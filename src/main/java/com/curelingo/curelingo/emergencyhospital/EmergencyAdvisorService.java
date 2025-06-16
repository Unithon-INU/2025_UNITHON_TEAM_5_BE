package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.curelingo.curelingo.emergencyhospital.stub.HospitalStubData;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
import com.curelingo.curelingo.gemini.prompt.GeminiEmergencyAdvisorPromptBuilder;
import com.curelingo.curelingo.gemini.service.GeminiEmergencyAdvisorService;
import com.curelingo.curelingo.gemini.dto.GeminiEmergencyAdvisorPromptResponse;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyAdvisorService {

    private final GeminiEmergencyAdvisorService advisorService;
    private final H3Service h3Service;

    public EmergencyAdviceResponse getRecommendedHospital(EmergencyAdviceRequest request) {
        String prompt = GeminiEmergencyAdvisorPromptBuilder.buildPrompt(request);
        GeminiEmergencyAdvisorPromptResponse response = advisorService.getRecommendation(prompt);
        log.info("[Gemini] Gemini API 응답 결과: {}", response);
        if (response == null) {
            log.warn("[Gemini] Gemini API returned null or missing recommended hospital");
            return new EmergencyAdviceResponse("No recommendation available", "");
        }
        return new EmergencyAdviceResponse(response.recommendedHospitalName(), response.recommendedReason());
    }


    /**
     * 입력 위치(위도, 경도)와 검색 반경(km) 내 인근 응급실(병원) 목록을 반환합니다.
     *
     * 동작 순서:
     *  1. 반경(km)에 따라 H3 해상도(res)와 gridDisk 반경(k)을 자동 계산합니다.
     *  2. H3 gridDisk로 인근 셀을 추출하고, 셀에 포함된 병원만 후보로 선정합니다.
     *  3. 각 병원까지의 실제 거리(km)를 계산해, 요청 반경 내 병원만 최종 반환합니다.
     *
     * @param lat     사용자 위도
     * @param lng     사용자 경도
     * @param radiusKm 검색 반경(킬로미터)
     * @return 인근 병원 정보 리스트(이름, 주소, hpid, 좌표, 거리 등)
     */
    public List<NearbyHospitalDto> findNearbyHospitals(double lat, double lng, double radiusKm) {
        // 반경에 따라 해상도(res)를 선택하고, gridDisk 반경(k) 계산
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Hospital] 반경 {}km → res={}, k={}", radiusKm, res, k);

        List<Hospital> hospitals = HospitalStubData.getAll();
        String userCell = h3Service.latLngToCell(lat, lng, res);

        // gridDisk로 인근 셀 리스트 추출
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Hospital] gridDisk 결과 셀 개수: {}", neighborCells.size());

        // 셀에 포함된 응급실만 후보로 선택
        List<Hospital> candidates = new ArrayList<>();
        for (Hospital h : hospitals) {
            String hospCell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
            if (neighborCells.contains(hospCell)) {
                candidates.add(h);
            }
        }
        log.info("[Hospital] H3 후보 응급실 개수: {}", candidates.size());

        // 실제 거리 계산 및 반경 내 응급실만 반환
        List<NearbyHospitalDto> result = new ArrayList<>();
        for (Hospital h : candidates) {
            double distanceKm = h3Service.calcDistanceKm(lat, lng, h.getLat(), h.getLng());
            log.debug("[Hospital] {} 거리: {}", h.getName(), distanceKm);
            if (distanceKm <= radiusKm) {
                result.add(new NearbyHospitalDto(h.getName(), h.getAddr(), h.getHpid(), h.getLat(), h.getLng(), distanceKm));
            }
        }
        log.info("[Hospital] 반경 {}km 이내 최종 병원 개수: {}", radiusKm, result.size());
        result.sort(Comparator.comparingDouble(NearbyHospitalDto::distanceKm));
        return result;
    }
}
