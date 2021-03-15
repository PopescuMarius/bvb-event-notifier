package com.scopert.bvbeventnotifier.attachments;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.scopert.bvbeventnotifier.smtp.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class DocumentProcessor {

    @Autowired
    private EmailSender emailSender;

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
                String textFromPage = PdfTextExtractor.getTextFromPage(reader, i);

                Optional<String> matchedPhrase = TrackedPhrases.containsTrackedPhrase(textFromPage);
                if (matchedPhrase.isPresent()) {
                    System.out.println(" $************ Interesting event detected *************$ " + symbol);
                    emailSender.alertUsers(symbol, matchedPhrase.get(), extractFileNameFromPath(path));
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

    private String extractFileNameFromUrl(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
    }

    private String extractFileNameFromPath(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('\\') + 1);
    }

}
