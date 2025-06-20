package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
import com.curelingo.curelingo.gemini.prompt.GeminiEmergencyAdvisorPromptBuilder;
import com.curelingo.curelingo.gemini.service.GeminiEmergencyAdvisorService;
import com.curelingo.curelingo.gemini.dto.GeminiEmergencyAdvisorPromptResponse;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import com.curelingo.curelingo.mongodb.HospitalRepository;
import com.curelingo.curelingo.emergencyhospital.mapper.HospitalMapper;
import com.curelingo.curelingo.mongodb.MongoHospital;
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
    private final HospitalRepository hospitalRepository;

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

    public List<NearbyHospitalDto> findNearbyERs(double lat, double lng, double radiusKm) {
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Hospital] 반경 {}km → res={}, k={}", radiusKm, res, k);

        List<MongoHospital> mongoHospitals = hospitalRepository.findByDutyEryn("1");
        List<Hospital> hospitals = mongoHospitals.stream()
                .map(HospitalMapper::toDomain)
                .toList();

        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Hospital] gridDisk 결과 셀 개수: {}", neighborCells.size());

        List<Hospital> candidates = new ArrayList<>();
        for (Hospital h : hospitals) {
            String hospCell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
            if (neighborCells.contains(hospCell)) {
                candidates.add(h);
            }
        }
        log.info("[Hospital] H3 후보 응급실 개수: {}", candidates.size());

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

    public List<NearbyHospitalDto> findAllNearbyHospitals(double lat, double lng, double radiusKm) {
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Hospital] 반경 {}km → res={}, k={}", radiusKm, res, k);

        List<MongoHospital> mongoHospitals = hospitalRepository.findAll();
        List<Hospital> hospitals = mongoHospitals.stream()
                .map(HospitalMapper::toDomain)
                .toList();

        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Hospital] gridDisk 결과 셀 개수: {}", neighborCells.size());

        List<Hospital> candidates = new ArrayList<>();
        for (Hospital h : hospitals) {
            String hospCell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
            if (neighborCells.contains(hospCell)) {
                candidates.add(h);
            }
        }
        log.info("[Hospital] H3 후보 응급실 개수: {}", candidates.size());

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