
package com.sales_scout.service.docs;

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
public class InvoiceDocService {

    public byte[] generateInvoiceDoc(StorageInvoice invoice) throws IOException {
        InputStream inputStream = new ClassPathResource("templates/Facture_storage.docx").getInputStream();
        XWPFDocument document = new XWPFDocument(inputStream);

        Map<String, String> placeholders = preparePlaceholders(invoice);
        replacePlaceholdersInDocument(document, placeholders);

        List<XWPFTable> tables = document.getTables();
//        if (invoice.getTotalHt() < invoice.getStorageContract().getMinimumBillingGuaranteed()) {
//            List<List<String>> combinedData = new ArrayList<>();
//            combinedData.add(List.of("Facturation sur la facturation minimale."));
//            fillTable(tables.get(1), combinedData);
//        }else {
//            List<List<String>> combinedData = new ArrayList<>();
//            combinedData.addAll(extractUnloadingTypes(invoice));
//            combinedData.addAll(extractStockedItemProvisions(invoice));
//            combinedData.addAll(extractRequirements(invoice));
//            fillTable(tables.get(1), combinedData);
//        }
        if (invoice.getTotalHt() < invoice.getStorageContract().getMinimumBillingGuaranteed()) {
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
            run.setText("Facturation sur la facturation minimale.");
            run.setBold(true);
            run.setFontSize(11);
        } else {
            List<List<String>> combinedData = new ArrayList<>();
            combinedData.addAll(extractUnloadingTypes(invoice));
            combinedData.addAll(extractStockedItemProvisions(invoice));
            combinedData.addAll(extractRequirements(invoice));
            fillTable(tables.get(1), combinedData);
        }

        List<List<String>> generalInfo = new ArrayList<>();
        generalInfo.add(Arrays.asList("Facture N° :", invoice.getNumber(),
                "Raison sociale :", invoice.getStorageContract().getCustomer().getName()));
        generalInfo.add(Arrays.asList(
                " Réf BL :", invoice.getStorageDeliveryNote().getNumber(),
                "Date d’émission  :", String.valueOf(LocalDate.now().getMonth()+"/"+LocalDate.now().getYear())));
        generalInfo.add(Arrays.asList(
                "Date Facture :", LocalDate.now().toString(),
                "ICE:", invoice.getStorageContract().getCustomer().getIce()));
        generalInfo.add(Arrays.asList(
                "", "",
                "Adresse :", invoice.getStorageContract().getCustomer().getCity().getName()
        ));
        fillTable(tables.get(0), generalInfo);


        if (invoice.getTotalHt() < invoice.getStorageContract().getMinimumBillingGuaranteed()) {
            List<List<String>> billingInfo = new ArrayList<>();
            double guaranteedHt = invoice.getStorageContract().getMinimumBillingGuaranteed();
            double tva = guaranteedHt * 0.2;
            double totalTtc = guaranteedHt + tva;

            billingInfo.add(Arrays.asList(
                    "TVA",
                    "20%",
                    String.format("%.2f",guaranteedHt),
                    String.format("%.2f",tva),
                    "",
                    String.format("%.2f",totalTtc),
                    totalTtc + " Dhs"
            ));
            fillTable(tables.get(2), billingInfo);
        }else {
            List<List<String>> billingInfo = new ArrayList<>();
            billingInfo.add(Arrays.asList(
                    "TVA", "20%", String.format("%.2f",invoice.getTotalHt()),
                    String.format("%.2f",invoice.getTva()),
                    "", String.format("%.2f",invoice.getTotalTtc())
                    , String.valueOf(invoice.getTotalTtc() + "Dhs")
            ));
            fillTable(tables.get(2), billingInfo);
        }

        List<List<String>> paymentInfo = new ArrayList<>();
        paymentInfo.add(Arrays.asList(
                invoice.getStorageContract().getPaymentMethod().getName(),
                String.valueOf(invoice.getStorageContract().getPaymentDeadline()),
                String.valueOf(LocalDate.now().plusDays(invoice.getStorageContract().getPaymentDeadline()))
        ));
        fillTable(tables.get(4), paymentInfo);

        List<List<String>> bankInfo = new ArrayList<>();
        bankInfo.add(Arrays.asList(
                "MA LOGISTICS", "011","", "0000012100061603","81"
        ));
        fillTable(tables.get(5), bankInfo);




        this.applyWhiteHeadersExceptFirstTable(document);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    private Map<String, String> preparePlaceholders(StorageInvoice invoice) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("Entreprise", String.valueOf(invoice.getStorageContract().getCompany().getName()));
        placeholders.put("customer", String.valueOf(invoice.getStorageContract().getCompany().getName()));
        return placeholders;
    }

    private List<List<String>> extractUnloadingTypes(StorageInvoice invoice) {
        List<List<String>> data = new ArrayList<>();
        for (StorageInvoiceStorageContractUnloadingType item : invoice.getStorageInvoiceStorageContractUnloadingTypes()) {
            if (item.getQuantity() > 0){
                data.add(Arrays.asList(
                        String.valueOf( "DEP"+item.getId()),
                        item.getStorageContractUnloadingType().getUnloadingType().getName(),
                        String.valueOf(item.getQuantity()),
                        String.format("%.2f",item.getStorageContractUnloadingType().getSalesPrice()),
                        "",
                        String.format("%.2f",item.getTotalHt())
                ));
            }
        }
        return data;
    }

    private List<List<String>> extractStockedItemProvisions(StorageInvoice invoice) {
        List<List<String>> data = new ArrayList<>();
        for (StorageInvoiceStorageContractStockedItemProvision item : invoice.getStorageInvoiceStorageContractStockedItemProvisions()) {
            if (item.getQuantity() > 0) {
                data.add(Arrays.asList(
                        String.valueOf("PRV" + item.getId()),
//                    String.valueOf( item.getRef().toString().length() > 8 ? item.getRef().toString().substring(0, 8) : item.getRef()),
                        item.getStockedItemProvision().getProvision().getName().concat(" " + item.getStockedItemProvision().getStockedItem().getSupport().getName()).concat(" " + item.getStockedItemProvision().getStockedItem().getStructure().getName()),
                        String.valueOf(item.getQuantity()),
                        String.format("%.2f",item.getStockedItemProvision().getSalesPrice()),
                        "",
                        String.format("%.2f",item.getTotalHt())
                ));
            }
        }
        return data;
    }

    private List<List<String>> extractRequirements(StorageInvoice invoice) {
        List<List<String>> data = new ArrayList<>();
        for (StorageInvoiceStorageContractRequirement item : invoice.getStorageInvoiceStorageContractRequirementList()) {
            if (item.getQuantity() > 0) {
                data.add(Arrays.asList(
                        String.valueOf("RQ" + item.getId()),
//                    String.valueOf( item.getRef().toString().length() > 8 ? item.getRef().toString().substring(0, 8) : item.getRef()),
                        item.getStorageContractRequirement().getRequirement().getName(),
                        String.valueOf(item.getQuantity()),
                        String.format("%.2f", item.getStorageContractRequirement().getSalesPrice()),
                        "",
                        String.format("%.2f",item.getTotalHt())

                ));
            }
        }
        return data;
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
