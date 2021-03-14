package com.scopert.bvbeventnotifier.attachments;

import com.scopert.bvbeventnotifier.utils.FilesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileCleaningTask {

    @Value("${folders.download}")
    protected String downloadFolder;

    @Scheduled(cron = "0 0 14 ? * SUN")
    public void runDuringOpenHours() throws IOException {

        List<Path> oldFiles = Files.walk(Paths.get(downloadFolder))
                .filter(Files::isRegularFile)
                .filter(FilesUtils::isOlderThan7Days)
                .collect(Collectors.toList());

        oldFiles.stream().forEach(FilesUtils::delete);

        log.info("Just cleaned {} files older than 7 days!", oldFiles.size());
    }

}
