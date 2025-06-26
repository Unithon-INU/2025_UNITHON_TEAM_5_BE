package com.curelingo.curelingo.clinic.dto;

public record NearbyClinicDto(
        String name,
        String nameEn,
        String address,
        String addressEn,
        String hpid,
        String tel,
        double distanceKm
) {
} 