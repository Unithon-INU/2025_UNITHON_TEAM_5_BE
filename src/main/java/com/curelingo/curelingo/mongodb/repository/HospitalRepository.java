package com.curelingo.curelingo.mongodb.repository;

import com.curelingo.curelingo.mongodb.MongoHospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HospitalRepository extends MongoRepository<MongoHospital, String> {

    // hpid 필드 기준 존재 여부 확인
    boolean existsByHpid(String hpid);
    
    // hpid로 병원 조회 (중복된 경우 첫 번째 결과 반환)
    MongoHospital findFirstByHpid(String hpid);
    
    // 응급실 운영 병원 조회
    List<MongoHospital> findByDutyEryn(String dutyEryn);
}
