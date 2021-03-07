package com.scopert.bvbeventnotifier.attachments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class FileManagement {

    @PostConstruct
    public void cleanOldFiles() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            Thread.currentThread().setName("file-cleaning-thread");
            try {
                Files.walk(Paths.get("temp/raw_reports"))
                     .filter(Files::isRegularFile)
                     .forEach(System.out::println);
            } catch (IOException e) {
                log.error("Could not clean old files", e);
            }
        });

    }

    @PostConstruct
    public void archiveUsefulFiles() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            Thread.currentThread().setName("file-archivingBvbCurrentReportsOpenHoursTask-thread");
        });
    }

}
