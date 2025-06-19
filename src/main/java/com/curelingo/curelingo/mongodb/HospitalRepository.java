package com.curelingo.curelingo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepository extends MongoRepository<Hospital, String> {

    // hpid 필드 기준 존재 여부 확인
    boolean existsByHpid(String hpid);
}
