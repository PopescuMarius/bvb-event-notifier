package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@ConditionalOnMissingBean(LocalEnvironmentTestingTask.class)
@Slf4j
public class BvbCurrentReportsWeekendTask extends BvbCurrentReportsTask{

    @Autowired
    public BvbCurrentReportsWeekendTask(CurrentReportsCrawler currentReportsCrawler) {
        this.currentReportsCrawler = currentReportsCrawler;
    }

    @PostConstruct
    @Override
    public void isUp() {
        log.info("{} is up and running!", BvbCurrentReportsWeekendTask.class.getSimpleName());
    }

    @Scheduled(cron = "0 01 * ? * SAT-SUN")
    public void runDuringTheWeekend() throws IOException {
        currentReportsCrawler.getLatestReportsOfToday(bvbUrl);
        log.debug("once per hour during weekend");
    }

}
