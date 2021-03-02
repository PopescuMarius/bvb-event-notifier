package com.scopert.bvbeventnotifier.crawler;

/**
 * Symbols are Filtered after I receive reports that can't be used:
 * - Very low volumes
 * - Bonds
 * - ETFs
 */
public enum UntrackedSymbols {
    IPRU("Very low volumes"),
    TVBETETF("Nothing to speculate from an ETF");

    private String reasonToIgnore;

    UntrackedSymbols(String s) {
        this.reasonToIgnore = s;
    }

    public static boolean isUntrackedSymbol(String value) {
        for (UntrackedSymbols symbol : UntrackedSymbols.values()) {
            if (symbol.name().equals(value.trim())) {
                return true;
            }
        }
        return false;
    }

}
