package com.scopert.bvbeventnotifier.crawler;

/**
 * Symbols are Filtered after I receive reports that can't be used:
 * - Very low volumes
 * - Bonds
 * - ETFs
 * - Certificat Turbo
 */
public enum UntrackedSymbols {

    NO_SYMBOL("News with no symbols have not been useful so far."){
        @Override
        String getName(){
            return "";
        }
    },

    IPRU("Very low volumes"),
    GRIU("Very low volumes"),
    VIV25E("Bond"),
    DRL22("Bond"),
    PBK28E("Bond"),
    AGR25("Bond"),
    AUT24E("Bond"),
    BNET22("Bond"),
    RBRO29("Bond"),
    TIM28("Bond"),
    BKAURTL12("Certificat Turbo"),
    BKWTITL8("Certificat Turbo"),
    EBBMWTL13 ("Certificat Turbo"),
    EBDBKTS16 ("Certificat Turbo"),
    BKDOWTSB6 ("Certificat Turbo"),
    EBSLVTS31 ("Certificat Turbo"),
    EBSLVTL41 ("Certificat Turbo"),
    TVBETETF("Nothing to speculate from an ETF"),
    DPW("Not from BVB market");

    private String reasonToIgnore;

    UntrackedSymbols(String s) {
        this.reasonToIgnore = s;
    }

    public static boolean isUntrackedSymbol(String value) {
        for (UntrackedSymbols symbol : UntrackedSymbols.values()) {
            if (symbol.getName().equals(value.trim())) {
                return true;
            }
        }
        return false;
    }

    String getName(){
        return super.name();
    }

}
