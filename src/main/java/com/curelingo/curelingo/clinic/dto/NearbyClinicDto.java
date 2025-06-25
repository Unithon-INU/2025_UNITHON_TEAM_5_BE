package com.curelingo.curelingo.clinic.dto;

import java.util.List;

public record NearbyClinicDto(
        String name,
        String address,
        String hpid,
        String type,
        String telephone,
        double lat,
        double lng,
        double distanceKm,
        List<String> departments  // 진료과목 목록 추가
) {
} 