package com.curelingo.curelingo.clinic;

import com.curelingo.curelingo.clinic.domain.Clinic;
import com.curelingo.curelingo.clinic.dto.NearbyClinicDto;
import com.curelingo.curelingo.clinic.mapper.ClinicMapper;
import com.curelingo.curelingo.location.H3ResolutionUtil;
import com.curelingo.curelingo.location.H3Service;
import com.curelingo.curelingo.mongodb.MongoHospital;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClinicService {

    private final H3Service h3Service;
    private final HospitalRepository hospitalRepository;



    /**
     * 진료과별 필터링된 인근 병원 검색 (반경 3km 고정)
     *
     * @param lat        현재 위도
     * @param lng        현재 경도
     * @param department 진료과 코드 (D001~D032)
     * @return 해당 진료과가 있는 인근 병원 목록
     */
    public List<NearbyClinicDto> findNearbyClinicsByDepartment(double lat, double lng, String department) {
        double radiusKm = 3.0; // 반경 3km 고정
        
        int res = H3ResolutionUtil.chooseResolution(radiusKm);
        int k = H3ResolutionUtil.kFromRadius(radiusKm, res);
        log.info("[Clinic] 진료과 검색 - 반경 {}km, 진료과: {} → res={}, k={}", radiusKm, department, res, k);

        // 모든 병원 조회
        List<MongoHospital> mongoHospitals = hospitalRepository.findAll();
        
        // 해당 진료과가 있는 병원만 필터링
        List<Clinic> clinicsWithDepartment = mongoHospitals.stream()
                .filter(mongoHospital -> mongoHospital.getDepartments() != null && 
                                       mongoHospital.getDepartments().contains(department))
                .map(ClinicMapper::toDomain)
                .toList();

        log.info("[Clinic] {} 진료과 보유 병원 수: {}", department, clinicsWithDepartment.size());

        String userCell = h3Service.latLngToCell(lat, lng, res);
        List<String> neighborCells = h3Service.gridDisk(userCell, k);
        log.info("[Clinic] gridDisk 결과 셀 개수: {}", neighborCells.size());

        // H3 셀 기반 1차 필터링
        List<Clinic> candidates = new ArrayList<>();
        for (Clinic clinic : clinicsWithDepartment) {
            String clinicCell = h3Service.latLngToCell(clinic.getLat(), clinic.getLng(), res);
            if (neighborCells.contains(clinicCell)) {
                candidates.add(clinic);
            }
        }
        log.info("[Clinic] H3 후보 {} 진료과 병원 개수: {}", department, candidates.size());

        // 정확한 거리 계산 및 2차 필터링
        List<NearbyClinicDto> result = new ArrayList<>();
        for (Clinic clinic : candidates) {
            double distanceKm = h3Service.calcDistanceKm(lat, lng, clinic.getLat(), clinic.getLng());
            log.debug("[Clinic] {} {} 진료과 거리: {}km", clinic.getName(), department, distanceKm);
            
            if (distanceKm <= radiusKm) {
                result.add(new NearbyClinicDto(
                        clinic.getName(),
                        clinic.getAddr(),
                        clinic.getHpid(),
                        clinic.getType(),
                        clinic.getTel(),
                        clinic.getLat(),
                        clinic.getLng(),
                        distanceKm,
                        clinic.getDepartments()
                ));
            }
        }

        log.info("[Clinic] 반경 {}km 이내 {} 진료과 최종 병원 개수: {}", radiusKm, department, result.size());
        
        // 거리 기준 오름차순 정렬
        result.sort(Comparator.comparingDouble(NearbyClinicDto::distanceKm));
        return result;
    }


} 