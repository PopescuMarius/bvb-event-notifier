package com.scopert.bvbeventnotifier.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final DateTimeFormatter BVB_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static String getCurrentDateInBVBFormat() {
        LocalDate currentDate = LocalDate.now();
        String currentDateInBvbFormat = currentDate.format(BVB_FORMAT);
        return currentDateInBvbFormat;
    }

}
