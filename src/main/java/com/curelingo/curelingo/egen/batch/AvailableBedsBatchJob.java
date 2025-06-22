package com.curelingo.curelingo.egen.batch;

import com.curelingo.curelingo.egen.EgenService;
import com.curelingo.curelingo.egen.dto.AvailableBedsItem;
import com.curelingo.curelingo.egen.dto.EgenResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvailableBedsBatchJob {

    private final EgenService egenService;
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void onStartUp() {
        log.info("[초기화] 서버 시작 시 응급 병상 데이터 최초 수집 실행");
        fetchAndSaveAvailableBeds();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void fetchAndSaveAvailableBeds() {
        try {
            int pageNo = 1;
            int numOfRows = 500;
            int totalUpdated = 0;

            while (true) {
                EgenResponse<AvailableBedsItem> response = egenService.getAvailableBeds(null, null, pageNo, numOfRows);
                List<AvailableBedsItem> items = response.getBody().getItems().getItem();

                if (items == null || items.isEmpty()) break;

                for (AvailableBedsItem item : items) {
                    Map<String, Object> beds = getAvailableBeds(item);
                    String hpid = item.getHpid();
                    String dutyName = item.getDutyName();
                    String dutyTel3 = item.getDutyTel3();
                    String hvidateStr = item.getHvidate();

                    LocalDateTime updatedAt = LocalDateTime.parse(hvidateStr, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                    Query query = new Query(Criteria.where("hpid").is(hpid));
                    Update update = new Update()
                            .set("dutyName", dutyName)
                            .set("dutyTel3", dutyTel3)
                            .set("updatedAt", updatedAt)
                            .set("beds", beds);

                    mongoTemplate.upsert(query, update, "emergency_bed_status");
                }

                totalUpdated += items.size();

                // 마지막 페이지 체크 (공공API가 totalCount 반환하면 더 안전)
                if (items.size() < numOfRows) break;
                pageNo++;
            }
            log.info("응급 병상 데이터 {}건 업데이트 완료", totalUpdated);

        } catch (Exception e) {
            log.error("[배치오류] 응급 병상 정보 수집 실패", e);
        }
    }

    private static Map<String, Object> getAvailableBeds(AvailableBedsItem item) {
        Map<String, Object> beds = new HashMap<>();
        beds.put("hvec", item.getHvec());
        beds.put("hvoc", item.getHvoc());
        beds.put("hvgc", item.getHvgc());
        beds.put("hvs01", item.getHvs01());
        beds.put("hv28", item.getHv28());
        beds.put("hvs02", item.getHvs02());
        beds.put("hvs22", item.getHvs22());
        beds.put("hvs38", item.getHvs38());
        beds.put("hv9", item.getHv9());
        beds.put("hvs14", item.getHvs14());
        beds.put("hv38", item.getHv38());
        beds.put("hvs21", item.getHvs21());
        beds.put("hv60", item.getHv60());
        beds.put("hvs60", item.getHvs60());
        return beds;
    }
}
