package com.scopert.bvbeventnotifier.crawler;

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
import java.nio.file.Files;
import java.nio.file.Paths;

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
            File destinationFile = new File("temp/reports/" + fileName);
            FileUtils.copyURLToFile(url, destinationFile);
            return destinationFile.getPath();
        } catch (MalformedURLException e) {
            log.error("Could not access file from URL: {}", pdfUrl);
        } catch (IOException e) {
            log.error("Could not download file from URL: {}", pdfUrl);
        }
        return null;
    }

    private boolean lookForTrackedPhrases(String symbol, String path) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(path);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
                if (textFromPage.contains(TrackedPhrases.SIGNIFICANT_CONTRACT.getValue())) {
                    System.out.println("MONEY ALERT!!!");
                    emailSender.sendEmail(symbol, TrackedPhrases.SIGNIFICANT_CONTRACT.getValue(), extractFileNameFromPath(path));
                    return true;
                }
            }
        } catch (IOException e) {
            log.error("Could not read file from path: {}", path);
        } finally {
            reader.close();
        }

        return false;
    }

    //TODO aici as vrea sa lansez si eu frumos un thread care sa faca curatenie cum facea george prin demeter
    private void deleteDownloadedFile(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            log.error("Could not delete file from path {}", path);
        }
    }

    private String extractFileNameFromUrl(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
    }

    private String extractFileNameFromPath(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('\\') + 1);
    }

}
