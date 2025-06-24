package com.curelingo.curelingo.mongodb;

import com.curelingo.curelingo.egen.dto.HospitalFullInfoItem;
import com.curelingo.curelingo.egen.EgenService;
import com.curelingo.curelingo.mongodb.dto.HospitalBasicDto;
import com.curelingo.curelingo.mongodb.dto.HospitalDto;
import com.curelingo.curelingo.mongodb.dto.HospitalRequestDto;
import com.curelingo.curelingo.mongodb.repository.HospitalRepository;
import com.curelingo.curelingo.translation.GoogleTranslationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final EgenService egenService;
    private final GoogleTranslationService translationService;

    public HospitalService(HospitalRepository hospitalRepository, 
                          EgenService egenService,
                          GoogleTranslationService translationService) {
        this.hospitalRepository = hospitalRepository;
        this.egenService = egenService;
        this.translationService = translationService;
    }

    public HospitalDto saveHospital(HospitalRequestDto requestDto) {
        HospitalDto dto = requestDto.toHospitalDto();
        
        if (hospitalRepository.existsByHpid(dto.getHpid())) {
            // 이미 존재하는 병원이면 기존 데이터 반환
            MongoHospital existing = hospitalRepository.findById(dto.getHpid()).orElse(null);
            if (existing != null) {
                return HospitalDto.from(existing);
            }
        }

        // 병원명과 주소 번역
        String dutyNameEn = translationService.translateToEnglish(dto.getDutyName());
        String dutyAddrEn = translationService.translateToEnglish(dto.getDutyAddr());

        MongoHospital mongoHospital = MongoHospital.builder()
                .hpid(dto.getHpid())
                .dutyName(dto.getDutyName())
                .dutyNameEn(dutyNameEn)  // 번역된 병원명
                .dutyAddr(dto.getDutyAddr())
                .dutyAddrEn(dutyAddrEn)  // 번역된 주소
                .dutyDivNam(dto.getDutyDivNam())
                .dutyEryn(dto.getDutyEryn())
                .dutyTel1(dto.getDutyTel1())
                .dutyTel3(dto.getDutyTel3())
                .dutyEtc(dto.getDutyEtc())
                .dutyTime1s(dto.getDutyTime1s())
                .dutyTime1c(dto.getDutyTime1c())
                .dutyTime2s(dto.getDutyTime2s())
                .dutyTime2c(dto.getDutyTime2c())
                .dutyTime3s(dto.getDutyTime3s())
                .dutyTime3c(dto.getDutyTime3c())
                .dutyTime4s(dto.getDutyTime4s())
                .dutyTime4c(dto.getDutyTime4c())
                .dutyTime5s(dto.getDutyTime5s())
                .dutyTime5c(dto.getDutyTime5c())
                .dutyTime6s(dto.getDutyTime6s())
                .dutyTime6c(dto.getDutyTime6c())
                .dutyTime7s(dto.getDutyTime7s())
                .dutyTime7c(dto.getDutyTime7c())
                .dutyTime8s(dto.getDutyTime8s())
                .dutyTime8c(dto.getDutyTime8c())
                .wgs84Lat(dto.getWgs84Lat())
                .wgs84Lon(dto.getWgs84Lon())
                .rnum(dto.getRnum())
                .build();

        MongoHospital savedHospital = hospitalRepository.save(mongoHospital);
        return HospitalDto.from(savedHospital);
    }

    public int saveHospitalMongo() {
        int totalSavedCount = 0;
        int pageNo = 1;
        int numOfRows = 1000; // 한 번에 1000개씩 가져오기
        boolean hasMoreData = true;

        while (hasMoreData) {
            try {
                // EgenService를 통해 데이터 가져오기
                var response = egenService.getFullData(pageNo, numOfRows);
                
                if (response == null || response.getBody() == null || response.getBody().getItems() == null) {
                    System.out.println("페이지 " + pageNo + "에서 데이터를 가져올 수 없습니다.");
                    break;
                }

                List<HospitalFullInfoItem> items = response.getBody().getItems().getItem();
                
                if (items == null || items.isEmpty()) {
                    System.out.println("페이지 " + pageNo + "에서 더 이상 데이터가 없습니다.");
                    hasMoreData = false;
                    break;
                }

                System.out.println("페이지 " + pageNo + "에서 " + items.size() + "개의 병원 데이터를 처리 중...");

                int pageSavedCount = 0;
                
                // 번역할 텍스트들을 수집 (배치 번역을 위해)
                List<String> hospitalNames = new ArrayList<>();
                List<String> hospitalAddresses = new ArrayList<>();
                List<HospitalFullInfoItem> validItems = new ArrayList<>();
                
                for (HospitalFullInfoItem item : items) {
                    // 필수 필드 검증
                    if (item.getHpid() == null || item.getHpid().trim().isEmpty() || "null".equals(item.getHpid())) {
                        continue; // hpid가 null이거나 빈 값이면 건너뛰기
                    }

                    // 중복 체크
                    if (hospitalRepository.existsByHpid(item.getHpid())) {
                        continue;
                    }
                    
                    validItems.add(item);
                    hospitalNames.add(safeString(item.getDutyName()));
                    hospitalAddresses.add(safeString(item.getDutyAddr()));
                }
                
                if (validItems.isEmpty()) {
                    pageNo++;
                    Thread.sleep(100);
                    continue;
                }
                
                // 배치 번역 실행
                List<String> translatedNames = null;
                List<String> translatedAddresses = null;
                
                System.out.println("번역 중... " + validItems.size() + "개 병원");
                
                translatedNames = translationService.translateMultipleToEnglish(hospitalNames);
                translatedAddresses = translationService.translateMultipleToEnglish(hospitalAddresses);
                
                // 검증된 아이템들을 저장
                for (int i = 0; i < validItems.size(); i++) {
                    HospitalFullInfoItem item = validItems.get(i);
                    
                    String dutyNameEn = (translatedNames != null && i < translatedNames.size()) 
                            ? translatedNames.get(i) : safeString(item.getDutyName());
                    String dutyAddrEn = (translatedAddresses != null && i < translatedAddresses.size()) 
                            ? translatedAddresses.get(i) : safeString(item.getDutyAddr());

                    // Hospital 엔티티 생성
                    MongoHospital mongoHospital = MongoHospital.builder()
                            .hpid(item.getHpid())
                            .dutyName(safeString(item.getDutyName()))
                            .dutyNameEn(dutyNameEn)  // 번역된 병원명
                            .dutyAddr(safeString(item.getDutyAddr()))
                            .dutyAddrEn(dutyAddrEn)  // 번역된 주소
                            .dutyDivNam(safeString(item.getDutyDivNam()))
                            .dutyEryn(safeString(item.getDutyEryn()))
                            .dutyTel1(safeString(item.getDutyTel1()))
                            .dutyTel3(safeString(item.getDutyTel3()))
                            .dutyEtc(safeString(item.getDutyEtc()))
                            .dutyTime1s(safeString(item.getDutyTime1s()))
                            .dutyTime1c(safeString(item.getDutyTime1c()))
                            .dutyTime2s(safeString(item.getDutyTime2s()))
                            .dutyTime2c(safeString(item.getDutyTime2c()))
                            .dutyTime3s(safeString(item.getDutyTime3s()))
                            .dutyTime3c(safeString(item.getDutyTime3c()))
                            .dutyTime4s(safeString(item.getDutyTime4s()))
                            .dutyTime4c(safeString(item.getDutyTime4c()))
                            .dutyTime5s(safeString(item.getDutyTime5s()))
                            .dutyTime5c(safeString(item.getDutyTime5c()))
                            .dutyTime6s(safeString(item.getDutyTime6s()))
                            .dutyTime6c(safeString(item.getDutyTime6c()))
                            .dutyTime7s(safeString(item.getDutyTime7s()))
                            .dutyTime7c(safeString(item.getDutyTime7c()))
                            .dutyTime8s(safeString(item.getDutyTime8s()))
                            .dutyTime8c(safeString(item.getDutyTime8c()))
                            .wgs84Lat(item.getWgs84Lat())
                            .wgs84Lon(item.getWgs84Lon())
                            .rnum(safeString(item.getRnum()))
                            .build();

                    hospitalRepository.save(mongoHospital);
                    pageSavedCount++;
                }

                totalSavedCount += pageSavedCount;
                System.out.println("페이지 " + pageNo + "에서 " + pageSavedCount + "개의 병원이 저장되었습니다.");

                // 다음 페이지로 이동
                pageNo++;

                // API 호출 간격 조절 (서버 부하 방지)
                Thread.sleep(100);

            } catch (Exception e) {
                System.err.println("페이지 " + pageNo + " 처리 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }

        System.out.println("총 " + totalSavedCount + "개의 병원이 저장되었습니다.");
        
        return totalSavedCount;
    }

    /**
     * null 값을 안전하게 문자열로 변환하는 헬퍼 메서드
     */
    private String safeString(String value) {
        if (value == null || "null".equals(value) || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    /**
     * 모든 병원 데이터를 삭제합니다.
     */
    public void deleteAllHospitals() {
        hospitalRepository.deleteAll();
        System.out.println("모든 병원 데이터가 삭제되었습니다.");
    }

    /**
     * hpid로 병원의 기본 정보(병원명, 주소)를 조회합니다.
     */
    public HospitalBasicDto getHospitalBasicInfo(String hpid, String language) {
        try {
            System.out.println("조회 시작 - hpid: " + hpid + ", language: " + language);
            
            MongoHospital hospital = hospitalRepository.findFirstByHpid(hpid);
            System.out.println("병원 조회 결과: " + (hospital != null ? "찾음" : "없음"));
            
            if (hospital == null) {
                return null;
            }
            
            System.out.println("병원명(한국어): " + hospital.getDutyName());
            System.out.println("병원명(영어): " + hospital.getDutyNameEn());
            System.out.println("주소(한국어): " + hospital.getDutyAddr());
            System.out.println("주소(영어): " + hospital.getDutyAddrEn());
            
            HospitalBasicDto result = HospitalBasicDto.from(hospital, language);
            System.out.println("DTO 변환 완료");
            
            return result;
        } catch (Exception e) {
            System.err.println("getHospitalBasicInfo 에러: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 언어에 따라 적절한 HospitalDto를 생성합니다.
     */
    private HospitalDto createLocalizedHospitalDto(MongoHospital mongoHospital, String language) {
        boolean useEnglish = "en".equalsIgnoreCase(language);
        
        return HospitalDto.builder()
                .hpid(mongoHospital.getHpid())
                .dutyName(useEnglish && mongoHospital.getDutyNameEn() != null 
                    ? mongoHospital.getDutyNameEn() : mongoHospital.getDutyName())
                .dutyNameEn(mongoHospital.getDutyNameEn())  // 항상 영어 필드 포함 (null이면 JSON에서 제외됨)
                .dutyAddr(useEnglish && mongoHospital.getDutyAddrEn() != null 
                    ? mongoHospital.getDutyAddrEn() : mongoHospital.getDutyAddr())
                .dutyAddrEn(mongoHospital.getDutyAddrEn())  // 항상 영어 필드 포함 (null이면 JSON에서 제외됨)
                .dutyDivNam(mongoHospital.getDutyDivNam())
                .dutyEryn(mongoHospital.getDutyEryn())
                .dutyTel1(mongoHospital.getDutyTel1())
                .dutyTel3(mongoHospital.getDutyTel3())
                .dutyEtc(mongoHospital.getDutyEtc())
                .dutyTime1s(mongoHospital.getDutyTime1s())
                .dutyTime1c(mongoHospital.getDutyTime1c())
                .dutyTime2s(mongoHospital.getDutyTime2s())
                .dutyTime2c(mongoHospital.getDutyTime2c())
                .dutyTime3s(mongoHospital.getDutyTime3s())
                .dutyTime3c(mongoHospital.getDutyTime3c())
                .dutyTime4s(mongoHospital.getDutyTime4s())
                .dutyTime4c(mongoHospital.getDutyTime4c())
                .dutyTime5s(mongoHospital.getDutyTime5s())
                .dutyTime5c(mongoHospital.getDutyTime5c())
                .dutyTime6s(mongoHospital.getDutyTime6s())
                .dutyTime6c(mongoHospital.getDutyTime6c())
                .dutyTime7s(mongoHospital.getDutyTime7s())
                .dutyTime7c(mongoHospital.getDutyTime7c())
                .dutyTime8s(mongoHospital.getDutyTime8s())
                .dutyTime8c(mongoHospital.getDutyTime8c())
                .wgs84Lat(mongoHospital.getWgs84Lat())
                .wgs84Lon(mongoHospital.getWgs84Lon())
                .rnum(mongoHospital.getRnum())
                .build();
    }
}
