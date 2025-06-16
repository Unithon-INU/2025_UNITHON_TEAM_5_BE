package com.curelingo.curelingo.emergencyhospital.dto;

public record NearbyHospitalDto(
        String name,
        String address,
        String hpid,
        double lat,
        double lng,
        double distanceKm
) {
}
