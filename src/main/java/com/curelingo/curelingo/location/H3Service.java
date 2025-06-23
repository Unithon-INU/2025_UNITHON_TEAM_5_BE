package com.curelingo.curelingo.location;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Uber H3 격자 시스템을 활용한 위치 기반 그리드 연산 서비스.
 * <p>
 * 위경도와 H3 셀 주소 변환, 셀 경계 계산, 반경 내 셀 조회 등 공간 검색 관련 기능을 제공합니다.
 * <p>
 * 병원, 약국 등 위치 기반 추천이나 인근 장소 검색 등에 사용합니다.
 */
@Slf4j
@Service
public class H3Service {
    private final H3Core h3;

    /**
     * H3Core 인스턴스를 초기화합니다.
     *
     * @throws RuntimeException H3Core 초기화 실패 시 발생
     */
    public H3Service() {
        try {
            this.h3 = H3Core.newInstance();
        } catch (IOException e) {
            throw new RuntimeException("H3Core init failed", e);
        }
    }

    /**
     * 위경도를 H3 셀 주소(해상도 지정)로 변환합니다.
     *
     * @param lat 위도
     * @param lng 경도
     * @param res H3 해상도(0~15, 숫자가 클수록 정밀)
     * @return H3 셀 주소(16진수 문자열)
     */
    public String latLngToCell(double lat, double lng, int res) {
        return h3.latLngToCellAddress(lat, lng, res);
    }

    /**
     * H3 셀 주소의 중심 좌표를 반환합니다.
     *
     * @param cellAddress H3 셀 주소(16진수 문자열)
     * @return 셀 중심 LatLng 좌표
     */
    public LatLng cellToLatLng(String cellAddress) {
        return h3.cellToLatLng(cellAddress);
    }

    /**
     * H3 셀의 경계 좌표(꼭짓점들)를 반환합니다.
     *
     * @param cellAddress H3 셀 주소(16진수 문자열)
     * @return LatLng 리스트(꼭짓점들, 순서 있음)
     */
    public List<LatLng> cellToBoundary(String cellAddress) {
        return h3.cellToBoundary(cellAddress);
    }

    /**
     * 주어진 셀 주소를 중심으로 k만큼 반경(그리드 기준) 내의 모든 셀을 조회합니다.
     *
     * @param cellAddress 중심 셀 주소
     * @param k           반경(그리드 단계)
     * @return 인접 셀 주소 리스트
     */
    public List<String> gridDisk(String cellAddress, int k) {
        return h3.gridDisk(cellAddress, k);
    }

    /**
     * 두 셀 주소 간 그리드 거리(격자상 이동 거리)를 계산합니다.
     *
     * @param a 셀 주소 A
     * @param b 셀 주소 B
     * @return 그리드 거리, 계산 불가시 -1
     */
    public long gridDistance(String a, String b) {
        try {
            return h3.gridDistance(a, b);
        } catch (Exception e) {
            return -1; // Return -1 if error (e.g., pentagon distortion)
        }
    }

    /**
     * 두 위경도 좌표 간 대권거리(최단 거리, km)를 계산합니다.
     *
     * @param lat1 위도1
     * @param lng1 경도1
     * @param lat2 위도2
     * @param lng2 경도2
     * @return 두 지점 간 거리(km)
     */
    public double calcDistanceKm(double lat1, double lng1, double lat2, double lng2) {
        return DistanceUtil.haversine(new LatLng(lat1, lng1), new LatLng(lat2, lng2));
    }
}
