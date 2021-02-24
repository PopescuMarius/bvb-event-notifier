package com.scopert.bvbeventnotifier.crawler;

import lombok.Getter;

@Getter
public enum TrackedPhrases {

    SIGNIFICANT_CONTRACT("contract semnificativ");

    private String value;

    TrackedPhrases(String s) {
        this.value = s;
    }
}
