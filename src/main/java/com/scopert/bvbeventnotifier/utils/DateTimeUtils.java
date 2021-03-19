package com.scopert.bvbeventnotifier.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final DateTimeFormatter BVB_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static final DateTimeFormatter NORMAL_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String getCurrentDateInBVBFormat() {
        LocalDate currentDate = LocalDate.now();
        String currentDateInBvbFormat = currentDate.format(BVB_FORMAT);
        return currentDateInBvbFormat;
    }

    public static String getCurrentDateInNormalFormat() {
        LocalDate currentDate = LocalDate.now();
        String currentDateInBvbFormat = currentDate.format(BVB_FORMAT);
        return currentDateInBvbFormat;
    }

}
