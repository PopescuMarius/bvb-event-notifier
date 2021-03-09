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

    public static boolean isRomanianFile(String url) {
        boolean isOtherLanguage = url.contains("-EN-") ||
                                  url.contains("-ENG.") ||
                                  url.contains("-en.");
        return !isOtherLanguage;
    }

    public DocumentWrapper(Document document) {
        this.wrappedDocument = document;
    }

    //TODO poate aici ar trebui sa iau doar ultimul element in prima faza, sa nu mai iau tot ?
    // cat iau in table asta, tot tabelul etc ? e de studiat aici la optimizare, sa fac un fel de if any updates get elements
    public String getLatestFoundReportDescription() {
        Element tableContent = getTableContent();
        return getEventDescriptionFrom(tableContent.children().get(0));
    }

    public Elements getReportsToBeProcessed(String lastProcessedReport) {
        Elements elements = getCurrentDayReportsForFollowedSymbols();
        Elements unprocessed = new Elements();
        for (Element e : elements) {
            String description = getEventDescriptionFrom(e);
            if (description.equals(lastProcessedReport)) {
                break;
            }
            unprocessed.add(e);
        }

        return unprocessed;
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

    private Elements getCurrentDayReportsForFollowedSymbols() {
        return getTableContent()
                .children()
                .stream()
                .filter(e -> e.child(3).text().startsWith(getCurrentDateInBVBFormat()))
                .filter(e -> !UntrackedSymbols.isUntrackedSymbol(e.child(0).select("strong").get(0).text()))
                .collect(toCollection(Elements::new));
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
