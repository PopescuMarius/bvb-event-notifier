package com.scopert.bvbeventnotifier.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    //TODO mocked for now. Where should I store it ? most basic/free database ?
    private String lastProcessedReport = "Deputy Chief Executive Officer mandate termination";

    public void getLatestReports(String URL) throws IOException {
        Document document = Jsoup.connect(URL).get();
        Elements reportsTable = document.select("table[id=gvv]");
        Element tableContent = reportsTable.get(0).children().get(1);

        String latestFoundReport = tableContent.children().get(0).child(2).attr("data-search");

        if (lastProcessedReport.equals(latestFoundReport)) {
            log.info("Processed 0 reports. Index is up to date");
            return;
        }

        for (Element row : tableContent.children()) {
            String symbol = row.child(0).select("strong").get(0).text();
            String description = row.child(2).attr("data-search");
            String publishDate = row.child(3).text();

            if (lastProcessedReport.equals(description)) {
                break;
            }

            if (TrackedSymbols.isTrackedSymbol(symbol)) {
                System.out.println(publishDate + " " + symbol + " " + description);
                Elements select = row.child(5).select("a[href]");
                for (Element e : select) {
                    String localeHref = e.attr("href");
                    String url = HOSTNAME + localeHref;
                    if (isRomanianFile(url)) {
                        documentProcessor.processAttachments(symbol, url);
                    }
                }
            }
        }

        lastProcessedReport = latestFoundReport;
        log.info("Processed {} new reports. Index has been updated with last processed report.", tableContent.children().size());
    }

    /* Most basic mode to eliminate english files. Maybe there are other languages or files that are not useful
       Also, there has to be a way to eliminate all non romanian files, I assume EN solves 80% though*/
    private boolean isRomanianFile(String url) {
        boolean isEnglishFile = url.contains("EN");
        return !isEnglishFile;
    }
}
