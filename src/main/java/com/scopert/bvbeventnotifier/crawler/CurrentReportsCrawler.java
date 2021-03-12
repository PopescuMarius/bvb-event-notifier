package com.scopert.bvbeventnotifier.crawler;

import com.scopert.bvbeventnotifier.attachments.DocumentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CurrentReportsCrawler {

    public static final String HOSTNAME = "https://www.bvb.ro";

    @Autowired
    DocumentProcessor documentProcessor;

    //TODO 1. am nevoie de postgresql ? este gratis si pe AWS ? poate pentru a parametra pe viitor fiecare user ce simoboluri are etc
    private String lastProcessedReport;

    public void getLatestReportsOfToday(String URL) throws IOException {
        DocumentWrapper bvbDocument = new DocumentWrapper(Jsoup.connect(URL).get());
        String latestFoundReportDescription = bvbDocument.getLatestFoundReportDescription();

        System.out.println("1. index is at :" + lastProcessedReport);
        if (latestFoundReportDescription.equals(lastProcessedReport)) {
            log.info("No new reports to process");
            return;
        }

        Elements unprocessedReports = bvbDocument.getReportsToBeProcessed(lastProcessedReport);

        for (Element row : unprocessedReports) {
            String symbol = bvbDocument.getEventSymbolFrom(row);
            System.out.println("*** " + symbol);
            bvbDocument.getAllAttachmentsFrom(row)
                       .stream()
                       .map(bvbDocument::computeUrlFromBVBPath)
                       .filter(Objects::nonNull)
                       .filter(DocumentWrapper::isRomanianFile)
                        //TODO aici pot face un thread pentru fiecare fisier
                       .forEach(url -> documentProcessor.processAttachments(symbol, url));
        }

        lastProcessedReport = latestFoundReportDescription;
        System.out.println("2. index is at :" + lastProcessedReport);
        log.info("Processed {} new reports. Index has been updated with last processed report.", unprocessedReports.size());
    }

}
