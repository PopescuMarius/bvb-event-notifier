package com.scopert.bvbeventnotifier.scheduler;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import org.springframework.beans.factory.annotation.Value;

public abstract class BvbCurrentReportsTask {

    @Value("${bvb.resources.starting-page}")
    protected String bvbUrl;

    protected CurrentReportsCrawler currentReportsCrawler;

    public abstract void isUp();

}
