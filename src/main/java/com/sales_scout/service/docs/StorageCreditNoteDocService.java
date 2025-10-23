
package com.sales_scout.service.docs;

import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStorageContractRequirement;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStorageContractStockedItemProvision;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStorageContractUnloadingType;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

@Service
public class StorageCreditNoteDocService {

    public byte[] generateCreditNoteDoc(StorageCreditNote creditNote) throws IOException {
        InputStream inputStream = new ClassPathResource("templates/credit_note_template.docx").getInputStream();
        XWPFDocument document = new XWPFDocument(inputStream);

        Map<String, String> placeholders = preparePlaceholders(creditNote);
        replacePlaceholdersInDocument(document, placeholders);

        List<XWPFTable> tables = document.getTables();


        XWPFTable table = tables.get(1);

        // Supprimer les lignes sauf l'entête
        while (table.getNumberOfRows() > 1) table.removeRow(1);

        // Créer une nouvelle ligne avec une seule cellule fusionnée
        XWPFTableRow row = table.createRow();

        for (int i = 1; i < 6; i++) {
            row.getCell(0).getCTTc().addNewTcPr().addNewGridSpan().setVal(BigInteger.valueOf(6));
            row.removeCell(i); // supprimer les autres
        }
        for (int i = 5; i >= 1; i--) {
            row.removeCell(i);
        }

        XWPFParagraph paragraph = row.getCell(0).getParagraphs().get(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setText("Avoir sur facture N° "+
                creditNote.getStorageInvoice().getNumber() +
                " le " + creditNote.getStorageInvoice().getInvoiceDate());
//        run.setBold(true);
        run.setFontSize(11);

        List<List<String>> generalInfo = new ArrayList<>();
        generalInfo.add(Arrays.asList(
                "Avior N° :", creditNote.getNumber(),
                "Raison sociale :", creditNote.getStorageInvoice().getStorageContract().getCustomer().getName()));

        generalInfo.add(Arrays.asList(
                "Date Avoir :", LocalDate.now().toString(),
                "ICE:", creditNote.getStorageInvoice().getStorageContract().getCustomer().getIce()));

        generalInfo.add(Arrays.asList(
                "Date d’émission :", LocalDate.now().toString(),
                "Adresse :", creditNote.getStorageInvoice().getStorageContract().getCustomer().getHeadOffice()));

        fillTable(tables.get(0), generalInfo);



        List<List<String>> billingInfo = new ArrayList<>();
        double totalHt = creditNote.getTotalHt();
        double tva = creditNote.getTva();
        double totalTtc = creditNote.getTotalTtc();

        billingInfo.add(Arrays.asList(
                "TVA",
                "20%",
                String.format("%.2f",totalHt),
                String.format("%.2f",tva),
                String.format("%.2f",totalTtc),
                totalTtc + " Dhs"
        ));
        fillTable(tables.get(2), billingInfo);
       this.applyWhiteHeadersExceptFirstTable(document);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    private Map<String, String> preparePlaceholders(StorageCreditNote creditNote) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("Entreprise", String.valueOf(creditNote.getStorageInvoice().getStorageContract().getCompany().getName()));
        placeholders.put("customer", String.valueOf(creditNote.getStorageInvoice().getStorageContract().getCompany().getName()));
        return placeholders;
    }


    private void replacePlaceholdersInDocument(XWPFDocument document, Map<String, String> placeholders) {
        document.getParagraphs().forEach(p -> replaceTextInParagraph(p, placeholders));
        document.getHeaderList().forEach(h -> h.getParagraphs().forEach(p -> replaceTextInParagraph(p, placeholders)));
        document.getFooterList().forEach(f -> f.getParagraphs().forEach(p -> replaceTextInParagraph(p, placeholders)));
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    cell.getParagraphs().forEach(p -> replaceTextInParagraph(p, placeholders));
                }
            }
        }
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        String fullText = paragraph.getText();
        if (fullText == null || fullText.isEmpty()) return;

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            fullText = fullText.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        int runCount = paragraph.getRuns().size();
        for (int i = runCount - 1; i >= 0; i--) paragraph.removeRun(i);

        XWPFRun run = paragraph.createRun();
        run.setText(fullText);
    }

    private void fillTable(XWPFTable table, List<List<String>> data) {
        while (table.getNumberOfRows() > 1) table.removeRow(1);

        for (List<String> rowData : data) {
            XWPFTableRow row = table.createRow();
            for (int i = 0; i < rowData.size(); i++) {
                XWPFTableCell cell = row.getCell(i);
                if (cell == null) cell = row.createCell();
                XWPFParagraph paragraph = cell.getParagraphs().get(0);
                XWPFRun run = paragraph.createRun();
                run.setText(rowData.get(i));
                run.setFontSize(11);
            }
        }
    }

    private void applyWhiteHeadersExceptFirstTable(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();

        for (int i = 0; i < tables.size(); i++) {
            if (i == 3) continue;
            XWPFTable table = tables.get(i);
            if (table.getNumberOfRows() > 0) {
                XWPFTableRow headerRow = table.getRow(0);
                for (XWPFTableCell cell : headerRow.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        for (XWPFRun run : paragraph.getRuns()) {
                            run.setColor("FFFFFF"); // Couleur blanche
                        }
                    }
                }
            }
        }
    }
}
