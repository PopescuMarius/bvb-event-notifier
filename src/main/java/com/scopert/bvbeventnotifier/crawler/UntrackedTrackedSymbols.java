package com.scopert.bvbeventnotifier.crawler;

/**
 * Symbols are Filtered after I receive reports that can't be used:
 * - Very low volumes
 * - Bonds
 * -
 */
public enum UntrackedTrackedSymbols {
    IPRU("Very low volumes");

    private String reasonToIgnore;

    UntrackedTrackedSymbols(String s) {
        this.reasonToIgnore = s;
    }

    public static boolean isUntrackedTrackedSymbol(String value) {
        for (UntrackedTrackedSymbols symbol : UntrackedTrackedSymbols.values()) {
            if (symbol.name().equals(value.trim())) {
                return true;
            }
        }
        return false;
    }

}
