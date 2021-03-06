package com.scopert.bvbeventnotifier.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler.HOSTNAME;
import static com.scopert.bvbeventnotifier.utils.DateTimeUtils.getCurrentDateInBVBFormat;
import static java.util.stream.Collectors.toCollection;

@Slf4j
public class DocumentWrapper {

    private Document wrappedDocument;

    public DocumentWrapper(Document document) {
        this.wrappedDocument = document;
    }

    public static boolean isRomanianFile(String url) {
        boolean isOtherLanguage = url.contains("-EN-") ||
                url.contains("-ENG.") ||
                url.contains("-EN.") ||
                url.contains("-en.");
        return !isOtherLanguage;
    }

    public static boolean isPdfFile(String url) {
        return url.endsWith(".pdf");
    }

    public String getLatestFoundReportDescription() {
        Element tableContent = getTableContent();
        return getEventDescriptionFrom(tableContent.children().get(0));
    }

    public Elements getReportsToBeProcessed(String lastProcessedReport) {
        Elements currentDayReports = getCurrentDayReports();

        Elements currentDayUnprocessedReports = new Elements();
        for (Element e : currentDayReports) {
            String description = getEventDescriptionFrom(e);
            if (description.equals(lastProcessedReport)) {
                break;
            }
            currentDayUnprocessedReports.add(e);
        }

        Elements filteredByUntrackedSymbols = currentDayUnprocessedReports
                .stream()
                .filter(e -> !UntrackedSymbols.isUntrackedSymbol(e.child(0).select("strong").get(0).text()))
                .filter(this::isErratum)
                .collect(toCollection(Elements::new));

        return filteredByUntrackedSymbols;
    }

    public String getEventSymbolFrom(Element row) {
        return row.child(0).select("strong").get(0).text();
    }

    public Elements getAllAttachmentsFrom(Element row) {
        return row.child(5).select("a[href]");
    }

    public String computeUrlFromBVBPath(Element attachment) {
        String href = attachment.attr("href");
        String url = href.startsWith("http") ? href : HOSTNAME + href;
        return url.replaceAll(" ", "%20");
    }

    private Elements getCurrentDayReports() {
        return getTableContent()
                .children()
                .stream()
                .filter(e -> e.child(3).text().startsWith(getCurrentDateInBVBFormat()))
                .collect(toCollection(Elements::new));
    }

    private boolean isErratum(Element e) {
        String description = getEventDescriptionFrom(e);
        if(description.contains("correction")){
            return false;
        }
        return true;
    }

    private String getEventDescriptionFrom(Element row) {
        return row.child(2).attr("data-search");
    }

    private Element getTableContent() {
        Elements reportsTable = wrappedDocument.select("table[id=gvv]");
        Element tableContent = reportsTable.get(0).children().get(1);
        return tableContent;
    }
}
