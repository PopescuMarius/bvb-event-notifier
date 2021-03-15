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
                    //TODO pe viitor ar trebui sa caute toate cuvintele cheie in raport. daca se face in || nu cred ca reprezinta o problema de performanta
                    System.out.println(" $************ Interesting event detected *************$ " + symbol);
                    emailSender.alertUsers(symbol, matchedPhrase.get(), extractFileNameFromPath(path));
                    //TODO send just one email per batch. sync collection to build the message and wait/join before sending the email
                   break;
                }
            }

        } catch (RuntimeException rt) {
            log.error("Runtime error while searching in PDF ", rt);
        } catch (IOException e) {
            //TODO most common issue that has to be solved : 'PDF header signature not found.'
            //TODO should not try to read non *.pdf files
            log.error("Could not read file from path: {} ", path);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    //TODO 2. ar trebuie marcate alea care au ceva interesant, pentru care am trimis alerta, mutate in alt loc sa fie dispo mai incolo
    //TODO 3. ar trebui astea marcate ca interesate sa le introduc in baza iar la final de zi sa iau si pretul, sa vad daca a crescut sau nu
    //TODO 4. de fapt ar trebui luat si pretul initial, daca e sau nu open piata, sa le pun in tabela. Poate si rulajul mediu ?
    //TODO 5. cred ca ar trebui luat si pretul sau graficul pe ultimele 7 zile, sa stim daca e deja prinsa sau nu stirea in el.

    private String extractFileNameFromUrl(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1);
    }

    private String extractFileNameFromPath(String pdfUrl) {
        return pdfUrl.substring(pdfUrl.lastIndexOf('\\') + 1);
    }

}
