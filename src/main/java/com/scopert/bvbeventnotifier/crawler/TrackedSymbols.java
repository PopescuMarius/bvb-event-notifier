package com.scopert.bvbeventnotifier.crawler;

public enum TrackedSymbols {
    SAFE;

    public static boolean isTrackedSymbol(String value) {
        for (TrackedSymbols symbol : TrackedSymbols.values()) {
            if (symbol.name().equals(value.trim())) {
                return true;
            }
        }
        return false;
    }
}
