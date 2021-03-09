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
    RANDAMENT_RECORD("randament record"),
    //3.
    ACTIUNI_GRATUITE1("acțiuni gratuite"),
    ACTIUNI_GRATUITE2("actiuni gratuite"),
    //4. majorare capital
    RATA_DE_SUBSCRIERE("Rata de subscriere"),
    MAJORARE_DE_CAPITAL1("majorare de capital"),
    MAJORARE_DE_CAPITAL2("majorarii capitalului social"),
    //5 DIVIDENDe
    DIVIDEND_SPECIAL("dividend special"),
    //6 Achizitie companii
    ACHIZITIE_COMPANIE_1A("promisiuni bilaterale de achiziționare"),
    ACHIZITIE_COMPANIE_1B("promisiuni bilaterale de achizitionare"),
    ACHIZITIE_COMPANIE_2A("dobândirea tuturor părților sociale"),
    ACHIZITIE_COMPANIE_2B("dobandirea tuturor partilor sociale");

    //TODO in baza tb sa am si evenimente asemanatoare -> data si ora, pretul inchidere pe urmatoarele 3 zile etc sa imi pot face o idee daca merita sau nu luat
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
