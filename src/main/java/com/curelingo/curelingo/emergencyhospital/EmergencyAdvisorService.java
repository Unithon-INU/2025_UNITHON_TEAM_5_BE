package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceRequest;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyAdviceResponse;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
import com.curelingo.curelingo.gemini.prompt.GeminiEmergencyAdvisorPromptBuilder;
import com.curelingo.curelingo.gemini.emergencyadvisor.GeminiEmergencyAdvisorService;
import com.curelingo.curelingo.gemini.emergencyadvisor.dto.GeminiEmergencyAdvisorPromptResponse;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import com.curelingo.curelingo.mongodb.repository.EmergencyBedStatusRepository;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import com.curelingo.curelingo.emergencyhospital.mapper.HospitalMapper;
import com.curelingo.curelingo.mongodb.MongoHospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyAdvisorService {

    private final GeminiEmergencyAdvisorService advisorService;
    private final H3Service h3Service;
    private final HospitalRepository hospitalRepository;
    private final EmergencyBedStatusRepository bedStatusRepository;

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

    public List<EmergencyBedStatus> findNearbyEmergencyBeds(double lat, double lng, double radiusKm) {
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[BedSearch] Search area: {} km, H3 resolution: {}, k: {}", radiusKm, res, k);

        List<MongoHospital> mongoHospitals = hospitalRepository.findByDutyEryn("1");
        List<Hospital> hospitals = mongoHospitals.stream()
                .map(m -> Hospital.builder()
                        .hpid(m.getHpid())
                        .name(m.getDutyName())
                        .tel(m.getDutyTel1())
                        .addr(m.getDutyAddr())
                        .lat(m.getWgs84Lat())
                        .lng(m.getWgs84Lon())
                        .build())
                .toList();

        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);

        List<Hospital> candidates = hospitals.stream()
                .filter(h -> {
                    String cell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
                    double dist = h3Service.calcDistanceKm(lat, lng, h.getLat(), h.getLng());
                    return neighborCells.contains(cell) && dist <= radiusKm;
                })
                .toList();

        List<String> hpidList = candidates.stream().map(Hospital::getHpid).toList();

        // 거리 정보 맵핑
        Map<String, Double> hpidToDistance = candidates.stream()
                .collect(Collectors.toMap(Hospital::getHpid,
                        h -> h3Service.calcDistanceKm(lat, lng, h.getLat(), h.getLng())));

        List<EmergencyBedStatus> beds = bedStatusRepository.findByHpidIn(hpidList);
        beds.forEach(b -> b.setDistanceKm(hpidToDistance.getOrDefault(b.getHpid(), null)));

        // 거리 기준 정렬
        List<EmergencyBedStatus> sorted = beds.stream()
                .sorted(Comparator.comparingDouble(b -> hpidToDistance.getOrDefault(b.getHpid(), Double.MAX_VALUE)))
                .toList();

        return sorted;
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