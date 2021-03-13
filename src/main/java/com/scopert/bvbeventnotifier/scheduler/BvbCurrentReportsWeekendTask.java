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
        //TODO cum fac sa interogheze url ul roman, nu cel englez ?
        currentReportsCrawler.getLatestReportsOfToday(bvbUrl);
        log.info("once per hour during weekend");
    }

}
