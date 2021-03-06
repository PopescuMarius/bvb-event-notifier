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

@Slf4j
@Service
public class CurrentReportsCrawler {

    public static final String HOSTNAME = "https://www.bvb.ro";

    @Autowired
    DocumentProcessor documentProcessor;

    //TODO 1. am nevoie de postgresql ? este gratis si pe AWS ? poate pentru a parametra pe viitor fiecare user ce simoboluri are etd
    //TODO Din momment ce e folosita de 3 taskuri separate trebuie vazut cum o pasez de la unu la altu, ca sutn 3 st deci instante diferite ale acc clase :) ...
    //     sa evitam concurentialitatea daca pun singleton etc ... hmmm poate bag o colectie, sa fie acolo ? sa vedem si prin ce stari a trecut
    private String lastProcessedReport;

    public void getLatestReportsOfToday(String URL) throws IOException {
        DocumentWrapper bvbDocument = new DocumentWrapper(Jsoup.connect(URL).get());
        String latestFoundReportDescription = bvbDocument.getLatestFoundReportDescription();

        if (latestFoundReportDescription.equals(lastProcessedReport)) {
            log.info("No new reports to process");
            return;
        }

        Elements unprocessedReports = bvbDocument.getUnprocessedReports(lastProcessedReport);

        for (Element row : unprocessedReports) {
            String symbol = bvbDocument.getEventSymbolFrom(row);

            bvbDocument.getAllAttachmentsFrom(row)
                       .stream()
                       .map(bvbDocument::computeUrlFromBVBPath)
                       .filter(Objects::nonNull)
                       .filter(this::isRomanianFile)
                       .forEach(url -> documentProcessor.processAttachments(symbol, url));
        }

        lastProcessedReport = latestFoundReportDescription;
        log.info("Processed {} new reports. Index has been updated with last processed report.", unprocessedReports.size());
    }

    /* Most basic mode to eliminate english files. Maybe there are other languages or files that are not useful
       Also, there has to be a way to eliminate all non romanian files, I assume EN solves 80% though*/
    private boolean isRomanianFile(String url) {
        boolean isEnglishFile = url.contains("EN")
                                || url.contains("en")
                                || url.contains("En")
                                || url.contains("engl")
                                || url.contains("de");
        return !isEnglishFile;
    }
}
