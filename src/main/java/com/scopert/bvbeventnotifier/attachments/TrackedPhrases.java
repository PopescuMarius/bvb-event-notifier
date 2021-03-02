package com.scopert.bvbeventnotifier.attachments;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum TrackedPhrases {
    //1. contract mare
    SIGNIFICANT_CONTRACT("contract semnificativ"),
    //2. rezultate bune
    MARJA_RECORD("marjă record"),
    PERFORMANTA_RECORD("o performanță record"),
    //3.
    ACTIUNI_GRATUITE("acțiuni gratuite"),
    //4. majorare capital
    RATA_DE_SUBSCRIERE("Rata de subscriere"),
    MAJORARE_DE_CAPITAL("majorare de capital"),
    MAJORARE_DE_CAPITAL2("majorarii capitalului social");

    private String value;

    TrackedPhrases(String s) {
        this.value = s;
    }

    public static Optional<String> containsTrackedPhrase(String line) {
        for (TrackedPhrases phrase : TrackedPhrases.values()) {
            if (line.contains(phrase.getValue())) {
                return Optional.of(phrase.getValue());
            }
        }
        return Optional.empty();
    }
}
