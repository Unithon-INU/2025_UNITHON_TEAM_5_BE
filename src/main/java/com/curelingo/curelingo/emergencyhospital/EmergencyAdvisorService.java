package com.curelingo.curelingo.emergencyhospital;

import com.curelingo.curelingo.emergencyhospital.domain.Hospital;
import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import com.curelingo.curelingo.emergencyhospital.dto.NearbyHospitalDto;
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

    private final H3Service h3Service;
    private final HospitalRepository hospitalRepository;
    private final EmergencyBedStatusRepository bedStatusRepository;

    public List<Map<String, Object>> findNearbyERs(double lat, double lon, String language) {
        double radiusKm = 5.0; // 반경 5km 고정
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Emergency] 반경 {}km, 언어: {} → res={}, k={}", radiusKm, language, res, k);

        List<MongoHospital> mongoHospitals = hospitalRepository.findByDutyEryn("1");
        List<Hospital> hospitals = mongoHospitals.stream()
                .map(m -> Hospital.builder()
                        .hpid(m.getHpid())
                        .name(m.getDutyName())
                        .nameEn(m.getDutyNameEn())
                        .addr(m.getDutyAddr())
                        .addrEn(m.getDutyAddrEn())
                        .tel(m.getDutyTel1())
                        .lat(m.getWgs84Lat())
                        .lng(m.getWgs84Lon())
                        .build())
                .toList();

        String userCell = h3Service.latLngToCell(lat, lon, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Emergency] gridDisk 결과 셀 개수: {}", neighborCells.size());

        List<Hospital> candidates = new ArrayList<>();
        for (Hospital h : hospitals) {
            String hospCell = h3Service.latLngToCell(h.getLat(), h.getLng(), res);
            if (neighborCells.contains(hospCell)) {
                candidates.add(h);
            }
        }
        log.info("[Emergency] H3 후보 응급실 개수: {}", candidates.size());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Hospital h : candidates) {
            double distanceKm = h3Service.calcDistanceKm(lat, lon, h.getLat(), h.getLng());
            log.debug("[Emergency] {} 거리: {}km", h.getName(), distanceKm);
            
            if (distanceKm <= radiusKm) {
                Map<String, Object> hospitalData = new HashMap<>();
                
                if ("en".equals(language)) {
                    // 영어 응답
                    hospitalData.put("nameEn", h.getNameEn() != null ? h.getNameEn() : h.getName());
                    hospitalData.put("addressEn", h.getAddrEn() != null ? h.getAddrEn() : h.getAddr());
                } else {
                    // 한국어 응답 (기본값)
                    hospitalData.put("name", h.getName());
                    hospitalData.put("address", h.getAddr());
                }
                
                hospitalData.put("hpid", h.getHpid());
                hospitalData.put("lat", h.getLat());
                hospitalData.put("lng", h.getLng());
                hospitalData.put("distanceKm", distanceKm);
                
                result.add(hospitalData);
            }
        }
        
        log.info("[Emergency] 반경 {}km 이내 최종 응급실 개수: {}", radiusKm, result.size());
        result.sort(Comparator.comparingDouble(data -> (Double) data.get("distanceKm")));
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
}