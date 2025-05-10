package com.curelingo.curelingo.emergencyhospital.dto;

public record HospitalCandidate(String name, int distanceMeters, int availableBeds, int totalBeds,
                                String congestionLevel, String department, boolean isNightOpen) {
}
