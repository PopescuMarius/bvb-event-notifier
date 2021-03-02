package com.scopert.bvbeventnotifier.crawler;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.scopert.bvbeventnotifier.crawler.CurrentReportsCrawler.HOSTNAME;
import static com.scopert.bvbeventnotifier.utils.DateTimeUtils.getCurrentDateInBVBFormat;
import static java.util.stream.Collectors.toCollection;

public class DocumentWrapper {

    private Document wrappedDocument;

    public DocumentWrapper(Document document) {
        this.wrappedDocument = document;
    }

    public String getLatestFoundReportDescription() {
        Element tableContent = getTableContent();
        return getEventDescriptionFrom(tableContent.children().get(0));
    }

    public Elements getCurrentDayReportsForFollowedSymbols() {
        return getTableContent()
                .children()
                .stream()
                .filter(e -> e.child(3).text().startsWith(getCurrentDateInBVBFormat()))
                .filter(e -> TrackedSymbols.isTrackedSymbol(e.child(0).select("strong").get(0).text()))
                .collect(toCollection(Elements::new));
    }

    public String getEventDescriptionFrom(Element row) {
        return row.child(2).attr("data-search");
    }

    public String getEventSymbolFrom(Element row) {
        return row.child(0).select("strong").get(0).text();
    }

    public Elements getAllAttachmentsFrom(Element row) {
        return row.child(5).select("a[href]");
    }

    public String computeURLfromBVBPath(Element attachment) {
        String localeHref = attachment.attr("href");
        String url = HOSTNAME + localeHref;
        return url;
    }

    private Element getTableContent() {
        Elements reportsTable = wrappedDocument.select("table[id=gvv]");
        Element tableContent = reportsTable.get(0).children().get(1);
        return tableContent;
    }
}
