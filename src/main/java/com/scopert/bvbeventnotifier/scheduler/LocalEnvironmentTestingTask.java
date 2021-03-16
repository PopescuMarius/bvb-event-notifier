package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Profile("local")
@Component
@Slf4j
public class LocalEnvironmentTestingTask extends BvbCurrentReportsTask {

    @Autowired
    public LocalEnvironmentTestingTask(CurrentReportsCrawler currentReportsCrawler) {
        this.currentReportsCrawler = currentReportsCrawler;
    }

    @PostConstruct
    @Override
    public void isUp() {
        log.info("{} is up and running!", LocalEnvironmentTestingTask.class.getSimpleName());
    }

    @Scheduled(cron = "0 * * * * *")
    public void runOnLocalEnvironment() throws IOException {
        currentReportsCrawler.getLatestReportsOfToday(bvbUrl);
        log.info("Testing on local environment Scheduled Task");
    }

}
