package com.scopert.bvbeventnotifier.attachments;

import com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler;
import com.scopert.bvbeventnotifier.scheduler.BvbCurrentReportsTask;
import com.scopert.bvbeventnotifier.utils.FilesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileCleaningTask {

    @Scheduled(cron = "0 0 14 ? * SUN")
    public void runDuringOpenHours() throws IOException {
        int nbOfDeletedFiles = 0;

        try {
            List<Path> oldFiles = Files.walk(Paths.get("temp/raw_reports"))
                    .filter(Files::isRegularFile)
                    .filter(FilesUtils::isOlderThan7Days)
                    .collect(Collectors.toList());

            nbOfDeletedFiles = oldFiles.size();
            oldFiles.stream().forEach(FilesUtils::delete);
        } catch (IOException e) {
            log.error("Could not clean old files", e);
        }

        log.info("Just cleaned {} files older than 7 days!", nbOfDeletedFiles);
    }

}
