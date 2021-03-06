package com.scopert.bvbeventnotifier.scheduler;

import org.springframework.beans.factory.annotation.Value;

public abstract class BvbCurrentReportsTask {

    @Value("${bvb.resources.starting-page}")
    protected String bvbUrl;

    public abstract void isUp();

}
