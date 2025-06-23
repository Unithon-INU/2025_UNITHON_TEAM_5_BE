package com.curelingo.curelingo.location;

import com.uber.h3core.util.LatLng;

/**
 * Haversine 공식 기반 두 위경도 좌표의 대권거리(최단 거리) 계산 유틸리티.
 * <p>
 * 위치 기반 추천, 반경 내 검색 등에 사용합니다.
 */
public class DistanceUtil {
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * 두 지점 간의 대권거리(km)를 계산합니다.
     *
     * @param p1 첫 번째 좌표(LatLng)
     * @param p2 두 번째 좌표(LatLng)
     * @return 두 지점 간 거리(km)
     */
    public static double haversine(LatLng p1, LatLng p2) {
        double lat1 = p1.lat, lng1 = p1.lng, lat2 = p2.lat, lng2 = p2.lng;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a));
    }
}
