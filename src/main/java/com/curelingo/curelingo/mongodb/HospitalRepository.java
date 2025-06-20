package com.curelingo.curelingo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HospitalRepository extends MongoRepository<MongoHospital, String> {

    // hpid 필드 기준 존재 여부 확인
    boolean existsByHpid(String hpid);
    List<MongoHospital> findAll();
    List<MongoHospital> findByDutyEryn(String dutyEryn);
}
