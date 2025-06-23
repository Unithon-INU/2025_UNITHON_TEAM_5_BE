package com.curelingo.curelingo.emergencyhospital.dto;

public record HospitalCandidate(
        String dutyName,
        String dutyTel3,
        String inpatientRoom,
        String generalICU,
        String internalMedicineICU,
        String surgicalICU,
        String neurologyWard,
        String ctAvailable,
        String mriAvailable,
        String ventilatorAvailable,
        String ambulanceAvailable
) {
}
