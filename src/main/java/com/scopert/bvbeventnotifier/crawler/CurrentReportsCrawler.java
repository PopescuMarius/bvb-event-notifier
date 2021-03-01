package com.scopert.bvbeventnotifier.crawler;

import com.scopert.bvbeventnotifier.attachments.DocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CurrentReportsCrawler {

    public static final String HOSTNAME = "https://www.bvb.ro";

    @Autowired
    DocumentProcessor documentProcessor;

    //TODO 1. trebuie bagat postgresql sa pot lansa un MVP rapid. este gratis si pe AWS

    private String lastProcessedReport = "Deputy Chief Executive Officer mandate termination";

    public void getLatestReportsOfToday(String URL) throws IOException {
        DocumentWrapper bvbDocument = new DocumentWrapper(Jsoup.connect(URL).get());
        String latestFoundReportDescription = bvbDocument.getLatestFoundReportDescription();

        if (lastProcessedReport.equals(latestFoundReportDescription)) {
            log.info("Processed 0 reports. Index is up to date");
            return;
        }

        Elements currentDayReports = bvbDocument.getCurrentDayReportsForFollowedSymbols();

        for (Element row : currentDayReports) {
            String symbol = bvbDocument.getEventSymbolFrom(row);
            String description = bvbDocument.getEventDescriptionFrom(row);

            if (lastProcessedReport.equals(description)) {
                break;
            }

            Elements attachments = bvbDocument.getAttachmentsFrom(row);
            for (Element attachment : attachments) {
                String url = bvbDocument.computeURLfromBVBPath(attachment);
                if (isRomanianFile(url)) {
                    documentProcessor.processAttachments(symbol, url);
                }
            }
        }

        lastProcessedReport = latestFoundReportDescription;
        log.info("Processed {} new reports. Index has been updated with last processed report.", currentDayReports.size());
    }

    /* Most basic mode to eliminate english files. Maybe there are other languages or files that are not useful
       Also, there has to be a way to eliminate all non romanian files, I assume EN solves 80% though*/
    private boolean isRomanianFile(String url) {
        boolean isEnglishFile = url.contains("EN");
        return !isEnglishFile;
    }
}
