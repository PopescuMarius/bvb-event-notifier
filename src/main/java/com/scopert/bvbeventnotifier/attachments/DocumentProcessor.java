package com.scopert.bvbeventnotifier.attachments;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.scopert.bvbeventnotifier.smtp.EmailHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class DocumentProcessor {

    public static final int PHRASE_OFFSET = 500;

    @Autowired
    private EmailHandler emailSender;

    public void processAttachments(String symbol, String pdfUrl) {
        String path = downloadFileLocally(pdfUrl);
        lookForTrackedPhrases(symbol, path);
    }

    private String downloadFileLocally(String pdfUrl) {
        try {
            String fileName = extractFileNameFromUrl(pdfUrl);
            URL url = new URL(pdfUrl);
            File destinationFile = new File("temp/raw_reports/" + fileName);
            FileUtils.copyURLToFile(url, destinationFile);
            return destinationFile.getPath();
        } catch (MalformedURLException e) {
            log.error("Could not access file from URL: {}", pdfUrl);
        } catch (IOException e) {
            log.error("Could not download file from URL: {}", pdfUrl);
        }
        return null;
    }

    private void lookForTrackedPhrases(String symbol, String path) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(path);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                String pageText = PdfTextExtractor.getTextFromPage(reader, i);

                Optional<Pair<TrackedEvents, String>> matchedPhrase = TrackedEvents.containsTrackedPhrase(pageText);
                if (matchedPhrase.isPresent()) {
                    String context = getPhraseContext(pageText, matchedPhrase.get().getSecond());
                    emailSender.alertUsers(symbol, matchedPhrase.get(), context, path);
                    break;
                }
            }
        } catch (RuntimeException rt) {
            log.error("Runtime error while searching in PDF ", rt);
        } catch (IOException e) {
            //TODO most common issue that has to be solved : 'PDF header signature not found.'
            log.error("Could not read file from path: {} ", path);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String getPhraseContext(String pageText, String phrase) {
        int phraseIndex = pageText.indexOf(phrase);
        int contextStartIndex = phraseIndex - PHRASE_OFFSET < 0 ? 0: phraseIndex - PHRASE_OFFSET;
        int contextEndIndex = phraseIndex + PHRASE_OFFSET > pageText.length() ? pageText.length() : phraseIndex + PHRASE_OFFSET;
        String context = pageText.substring(contextStartIndex, contextEndIndex);
        return context;
    }

    private String extractFileNameFromUrl(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
    }

}
