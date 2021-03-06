package com.scopert.bvbeventnotifier.crawler;

import com.scopert.bvbeventnotifier.attachments.DocumentProcessor;
import com.scopert.bvbeventnotifier.storage.model.BvbLastProcessedNews;
import com.scopert.bvbeventnotifier.storage.repository.BvbLastProcessedNewsRepository;
import com.scopert.bvbeventnotifier.storage.services.BvbLastProcessedNewsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Service
public class CurrentReportsCrawler {

    public static final String HOSTNAME = "https://www.bvb.ro";

    @Autowired
    DocumentProcessor documentProcessor;

    @Autowired
    BvbLastProcessedNewsService bvbLastProcessedNewsService;

    public void getLatestReportsOfToday(String URL) throws IOException {
        DocumentWrapper bvbDocument = new DocumentWrapper(Jsoup.connect(URL).get());
        String latestFoundReportDescription = bvbDocument.getLatestFoundReportDescription();
        String lastProcessedReport = bvbLastProcessedNewsService.findTopByOrderByIdDesc().getNewsTitle();

        log.debug("1. index is at :" + lastProcessedReport);
        if (latestFoundReportDescription.equals(lastProcessedReport)) {
            log.debug("No new reports to process");
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
                       .filter(DocumentWrapper::isPdfFile)
                        //TODO process on different threads ?
                       .forEach(url -> documentProcessor.processAttachments(symbol, url));
        }

        BvbLastProcessedNews savedIndex = bvbLastProcessedNewsService.saveLastProcesedNews(latestFoundReportDescription);
        log.debug("2. index is at :" + savedIndex.getNewsTitle());
        log.debug("Processed {} new reports. Index has been updated with last processed report.", unprocessedReports.size());
    }

}
