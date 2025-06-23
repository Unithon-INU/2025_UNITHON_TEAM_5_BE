package com.curelingo.curelingo.location;

/**
 * 반경(km)에 따라 적정 H3 해상도 및 gridDisk k값을 계산하는 유틸리티.
 * <p>
 * 반경별 적절한 해상도, 해상도별 실제 반경, gridDisk k 계산 로직 포함.
 * <p>
 * res(H3 Resolution): H3 셀의 해상도(0~15). 숫자가 높을수록 셀이 작아짐.
 * k(gridDisk의 반경): 중심 셀에서 몇 겹의 hex ring까지 확장할지 결정.
 *    즉, k가 커질수록 더 넓은 범위를 커버함.
 * <p>
 * 참고: H3 공식 해상도별 셀 크기 표 - https://h3geo.org/docs/core-library/restable/
 */
public class H3ResolutionUtil {
    // H3 공식 평균 셀 반지름 (km)
    public static final double[] HEX_RADIUS_KM = {
            1107.712591, 418.6760055, 158.2446558, 59.81085794, 22.61292288,
            8.553576119, 3.23615609, 1.2249653, 0.46356654, 0.17579878,
            0.06668888, 0.02530122, 0.00959596, 0.00364068, 0.00138134, 0.0005246
    };

    /**
     * 주어진 반경(km)에 적합한 H3 해상도를 반환합니다.
     *
     * @param radiusKm 검색 반경(km)
     * @return H3 해상도(0~15)
     */
    public static int chooseResolution(double radiusKm) {
        if (radiusKm <= 0.5) return 10;
        if (radiusKm <= 1.5) return 9;
        if (radiusKm <= 3) return 8;
        if (radiusKm <= 8) return 7;
        return 6;
    }

    /**
     * 해상도(res) 기준으로 반경(km)에 필요한 gridDisk k값을 반환합니다.
     *
     * @param radiusKm 반경(km)
     * @param res      H3 해상도(0~15)
     * @return gridDisk용 k값(1 이상)
     */
    public static int kFromRadius(double radiusKm, int res) {
        double hexRadius = HEX_RADIUS_KM[res];
        return Math.max(1, (int)Math.ceil(radiusKm / hexRadius) + 1); // 안전하게 1 더
    }
}