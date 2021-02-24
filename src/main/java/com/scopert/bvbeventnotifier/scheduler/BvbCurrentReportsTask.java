package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class BvbCurrentReportsTask {

    @Value("${bvb.resources.starting-page}")
    private String bvbUrl;

    @Autowired
    private CurrentReportsCrawler currentReportsCrawler;

    @Scheduled(fixedRateString  = "${scheduled.task.frequency.every-minute}")
    public void scheduleTaskWithFixedRate() throws IOException {
        currentReportsCrawler.getLatestReports(bvbUrl);
        log.info("Parsing BVB is LIVE");
    }

}
