package com.scopert.bvbeventnotifier.crawler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentWrapperTest {

    @Test
    void should_manage_EN_for_identified_case() {
        String englishFile = "https://www.bvb.ro/info/Raportari/NRF/NRF_20210308165755_Current-Report-11-NRF-EN-vf.pdf";
        String englishFile1 = "https://www.bvb.ro/infocont/infocont21/M_20210308101242_RC-138-EN-Notificare-rascumparare-actiuni-1-5-march-2021.pdf";
        String romanianFile = "https://www.bvb.ro/info/Raportari/NRF/NRF_20210308165755_RAPORT-CURENT-11-NRF-RO-vf.pdf";
        String romanianFile1 = "https://www.bvb.ro/infocont/infocont21/M_20210308101242_RC-138-RO-Notificare-rascumparare-actiuni-1-5-martie-2021.pdf";
        assertEquals(false, DocumentWrapper.isRomanianFile(englishFile));
        assertEquals(false, DocumentWrapper.isRomanianFile(englishFile1));
        assertEquals(true, DocumentWrapper.isRomanianFile(romanianFile));
        assertEquals(true, DocumentWrapper.isRomanianFile(romanianFile1));
    }

    @Test
    void should_manage_ENG_for_identified_case() {
        String englishFile = "https://www.bvb.ro/infocont/infocont21/COTE_20210308153711_COTE-08-03-2021-RC-8-BVB-si-ASF-ENG.pdf";
        String romanianFile = "https://www.bvb.ro/infocont/infocont21/COTE_20210308153711_COTE-08-03-2021-RC-8-BVB-si-ASF.pdf";
        assertEquals(false, DocumentWrapper.isRomanianFile(englishFile));
        assertEquals(true, DocumentWrapper.isRomanianFile(romanianFile));
    }

    @Test
    void should_manage_en_for_identified_case() {
        String englishFile = "https://www.bvb.ro/infocont/infocont21/SIF2_20210308145953_20210308-Finalizare-etapa-a-II-a-de-rascumparare---en.pdf";
        String englishFile1 = "https://www.bvb.ro/infocont/infocont21/CEON_20210308131318_rap-curent-publicare-proiect-fuziune-site-en.pdf";
        String romanianFile = "https://www.bvb.ro/infocont/infocont21/SIF2_20210308145953_20210308-Finalizare-etapa-a-II-a-de-rascumparare.pdf";
        String romanianFile1 = "https://www.bvb.ro/infocont/infocont21/CEON_20210308131318_rap-curent-publicare-proiect-fuziune-site.pdf";
        assertEquals(false, DocumentWrapper.isRomanianFile(englishFile));
        assertEquals(true, DocumentWrapper.isRomanianFile(romanianFile));
        assertEquals(false, DocumentWrapper.isRomanianFile(englishFile1));
        assertEquals(true, DocumentWrapper.isRomanianFile(romanianFile1));
    }

}