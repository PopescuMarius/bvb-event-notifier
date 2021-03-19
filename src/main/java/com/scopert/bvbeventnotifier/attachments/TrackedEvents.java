package com.scopert.bvbeventnotifier.attachments;

import lombok.Getter;
import org.springframework.data.util.Pair;

import java.util.Optional;

@Getter
public enum TrackedEvents {

    CONTRACT_SEMNIFICATIV(new String[]{"contract semnificativ"}),
    PERFORMANTA_RECORD(new String[]{"marjă record", "o performanță record", "randament record"}),
    ACTIUNI_GRATUITE(new String[]{"acțiuni gratuite", "actiuni gratuite", "Rata de subscriere"}),
    MAJORARE_CAPITAL(new String[]{"majorare de capital", "majorarii capitalului social"}),
    ACHIZITIE_COMPANIE(new String[]{"promisiuni bilaterale de achiziționare", "promisiuni bilaterale de achizitionare",
                                    "dobândirea tuturor părților sociale", "dobandirea tuturor partilor sociale", "aceasta achizitie","această achiziție"}),
    DIVIDEND_SPECIAL(new String[]{"dividend special"});

    private String[] values;

    TrackedEvents(String[] keyPhrases) {
        this.values = keyPhrases;
    }

    public static Optional<Pair<TrackedEvents, String>> containsTrackedPhrase(String line) {
        for (TrackedEvents event : TrackedEvents.values()) {
            for (String phrase : event.getValues()) {
                if (line.contains(phrase)) {
                    return Optional.of(Pair.of(event, phrase));
                }
            }
        }
        return Optional.empty();
    }
}
