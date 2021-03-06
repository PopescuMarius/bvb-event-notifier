package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Slf4j
public class BvbCurrentReportsOpenHoursTask extends BvbCurrentReportsTask{

    @Autowired
    private CurrentReportsCrawler currentReportsCrawler;

    @PostConstruct
    @Override
    public void isUp() {
        log.info("{} is up and running!", BvbCurrentReportsOpenHoursTask.class.getSimpleName());
    }

    @Scheduled(cron = "0 * 08-19 ? * MON-FRI")
    public void runDuringOpenHours() throws IOException {
        currentReportsCrawler.getLatestReportsOfToday(bvbUrl);
        log.debug("every minute during open hours and close to them");
    }

}
