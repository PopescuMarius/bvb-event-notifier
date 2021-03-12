package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Slf4j
public class BvbCurrentReportsClosedHoursTask extends BvbCurrentReportsTask{

    @Autowired
    public BvbCurrentReportsClosedHoursTask(CurrentReportsCrawler currentReportsCrawler) {
        this.currentReportsCrawler = currentReportsCrawler;
    }

    @PostConstruct
    @Override
    public void isUp() {
        log.info("{} is up and running!", BvbCurrentReportsClosedHoursTask.class.getSimpleName());
    }

    @Scheduled(cron = "0 */30 00-08,19-23 ? * MON-FRI")
    public void runDuringClosedHours() throws IOException {
        currentReportsCrawler.getLatestReportsOfToday(bvbUrl);
        log.info("every 30 min during closed hours");
    }

}
