package com.scopert.bvbeventnotifier.attachments;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum TrackedPhrases {

    SIGNIFICANT_CONTRACT("contract semnificativ"),
    PERFORMANTA_RECORD1("marjă record"),
    PERFORMANTA_RECORD2("o performanță record"),
    PERFORMANTA_RECORD3("randament record"),
    ACTIUNI_GRATUITE1("acțiuni gratuite"),
    ACTIUNI_GRATUITE2("actiuni gratuite"),
    ACTIUNI_GRATUITE3("Rata de subscriere"),
    MAJORARE_DE_CAPITAL1("majorare de capital"),
    MAJORARE_DE_CAPITAL2("majorarii capitalului social"),
    DIVIDEND_SPECIAL("dividend special"),
    ACHIZITIE_COMPANIE_1A("promisiuni bilaterale de achiziționare"),
    ACHIZITIE_COMPANIE_1B("promisiuni bilaterale de achizitionare"),
    ACHIZITIE_COMPANIE_2A("dobândirea tuturor părților sociale"),
    ACHIZITIE_COMPANIE_2B("dobandirea tuturor partilor sociale"),
    //TODO de studiat open-close price si data mesajului primit pentru M in data de 15.03
    //TODO cred ca ma va interesa si rulajul mediu, sa imi dau seama cat as putea sa cumpar
    ACHIZITIE_COMPANIE_3A("aceasta achizitie"),
    ACHIZITIE_COMPANIE_3B("această achiziție");

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
