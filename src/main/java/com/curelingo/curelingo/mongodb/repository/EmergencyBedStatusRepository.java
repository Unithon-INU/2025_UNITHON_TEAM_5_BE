package com.curelingo.curelingo.mongodb.repository;

import com.curelingo.curelingo.emergencyhospital.dto.EmergencyBedStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyBedStatusRepository extends MongoRepository<EmergencyBedStatus, String> {
    // hpid가 여러 개일 때 한번에 조회
    List<EmergencyBedStatus> findByHpidIn(List<String> hpid);
}
