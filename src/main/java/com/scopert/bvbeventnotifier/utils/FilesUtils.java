package com.scopert.bvbeventnotifier.utils;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;

public class FilesUtils {

    @SneakyThrows
    public static boolean isOlderThan7Days(java.nio.file.Path path) {
        FileTime lastModifiedTime = Files.getLastModifiedTime(path);
        Duration interval = Duration.between(lastModifiedTime.toInstant(), Instant.now());
        return interval.toDays() > 7;
    }

    @SneakyThrows
    public static void delete(java.nio.file.Path path) {
        Files.delete(path);
    }

}
