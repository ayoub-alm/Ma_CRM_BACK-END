package com.sales_scout.service.docs;

import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class DocxService {

    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;

    public DocxService(StorageOfferStockedItemRepository stockedItemRepository) {
        this.storageOfferStockedItemRepository = stockedItemRepository;
    }

    public byte[] generateContractDocx(Map<String, String> placeholders, List<List<String>> tableData1, List<List<String>> tableData2, List<List<String>> tableData3) throws IOException {
        // Load the DOCX template
        File templateFile = new ClassPathResource("templates/Contrat.docx").getFile();
        FileInputStream fis = new FileInputStream(templateFile);
        XWPFDocument document = new XWPFDocument(fis);

        // Replace placeholders in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceText(paragraph, placeholders);
        }

        // Get tables from the document
        List<XWPFTable> tables = document.getTables();

        // Fill tables dynamically (assuming document has 3 tables)
        if (tables.size() >= 3) {
            fillTable(tables.get(0), tableData1);  // First table
            fillTable(tables.get(1), tableData2);  // Second table
            fillTable(tables.get(2), tableData3);  // Third table
        }

        // Save updated document to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    private void replaceText(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    String replacement = entry.getValue() != null ? entry.getValue() : "";  // ✅ Fix: Replace null with empty string
                    text = text.replace("{{" + entry.getKey() + "}}", replacement);
                }
                run.setText(text, 0);
            }
        }
    }

    private void fillTable(XWPFTable table, List<List<String>> tableData) {
        // Remove existing rows (except the first one, assuming it's a header)
        while (table.getNumberOfRows() > 1) {
            table.removeRow(1);
        }

        // Insert new rows with proper cell initialization
        for (List<String> rowData : tableData) {
            XWPFTableRow row = table.createRow();

            for (int i = 0; i < rowData.size(); i++) {
                XWPFTableCell cell = row.getCell(i);

                // Ensure the cell exists before setting text
                if (cell == null) {
                    cell = row.createCell();
                }

                cell.setText(rowData.get(i) != null ? rowData.get(i) : ""); // Avoid null values
            }
        }
    }


    /**
     *
     * @param offer
     * @return
     * @throws IOException
     */
    public byte[] generateStorageOfferDoc(StorageOffer offer) throws IOException {
        // Load the DOCX template
        File templateFile = new ClassPathResource("templates/offer.docx").getFile();
        FileInputStream fis = new FileInputStream(templateFile);
        XWPFDocument document = new XWPFDocument(fis);

        // Prepare placeholders
        Map<String, String> placeholders = preparePlaceholders(offer);

        // Replace text placeholders in paragraphs
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceText(paragraph, placeholders);
        }

        // Get tables from the document
        List<XWPFTable> tables = document.getTables();

        // Fill tables dynamically (ensure the template has enough tables)
        if (tables.size() >= 3) {
            fillTable(tables.get(0), extractStockedItemsData(offer)); // Stocked items table
            fillTable(tables.get(1), extractUnloadingTypeDataForOffer(offer));      // Dépotage - Tarifs table
            fillTable(tables.get(2), extractOperationsData(offer));    // Opérations logistiques - Tarifs table
        }

        // Save updated document to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    private Map<String, String> preparePlaceholders(StorageOffer offer) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("Référence", offer.getRef() != null ? offer.getRef().toString() : "N/A");
        placeholders.put("Date_offre", offer.getCreatedAt() != null ? offer.getCreatedAt().toLocalDate().toString() : "N/A");
        placeholders.put("Fin_de_validite", offer.getExpirationDate() != null ? offer.getExpirationDate().toLocalDate().toString() : "N/A");
        placeholders.put("Cree_par", offer.getInterlocutor() != null ? offer.getInterlocutor().getFullName() : "N/A");
        placeholders.put("Email", offer.getInterlocutor() != null ? offer.getInterlocutor().getEmailAddress().getAddress() : "N/A");
        placeholders.put("Entreprise", offer.getCompany() != null ? offer.getCompany().getName() : "N/A");
        placeholders.put("Type_de_produit", offer.getProductType() != null ? offer.getProductType() : "N/A");
        placeholders.put("Duree_de_stockage", offer.getDuration() != null ? String.valueOf(offer.getDuration()) : "0");
        placeholders.put("Raison_de_stockage", offer.getStorageReason() != null ? offer.getStorageReason().name() : "N/A");
        placeholders.put("SKU_value", offer.getNumberOfSku() > 0 ? String.valueOf(offer.getNumberOfSku()) : "0");
        placeholders.put("Livre", offer.getLiverStatus() != null ? offer.getLiverStatus().name() : "N/A");
        placeholders.put("Facturation_minimale_assuree", "1000 MAD"); // Example value
        placeholders.put("Emplacements_palettes_reserves", "20"); // Example value
        placeholders.put("Valeur_de_frais_de_gestion", "500 MAD"); // Example value
        placeholders.put("Notes", "Conditions spécifiques à discuter.");

        return placeholders;
    }


    private List<List<String>> extractStockedItemsData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        List<StorageOfferStockedItem> storageOfferStockedItems = storageOfferStockedItemRepository.findAllByStorageOfferId(offer.getId());
        for (StorageOfferStockedItem item : storageOfferStockedItems) {
            data.add(Arrays.asList(
                    item.getStockedItem().getSupport().getName(),
                    String.valueOf(item.getStockedItem().getWeight()),
                    item.getStockedItem().getDimension().getLength() + "x" + item.getStockedItem().getDimension().getWidth() + "x" + item.getStockedItem().getDimension().getHeight()
            ));
        }
        return data;
    }

    private List<List<String>> extractUnloadingTypeDataForOffer(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        offer.getStorageOfferUnloadingTypes().forEach(storageOfferUnloadType -> {
            data.add(Arrays.asList(storageOfferUnloadType.getUnloadingType().getName(),
                    storageOfferUnloadType.getUnloadingType().getUnitOfMeasurement(),
                    storageOfferUnloadType.getSalesPrice().toString()));
        });
        return data;
    }

    private List<List<String>> extractOperationsData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        offer.getStockedItems().forEach(item -> {
            data.add(Arrays.asList(
                    item.getSupport().getName()+ " " + item.getStructure().getName() ,
                    item.getWeight().toString(),
                    item.getDimension().getWidth().toString() + "x" +   item.getDimension().getLength().toString() + "x" +  item.getDimension().getHeight().toString()
                    )
            );
        });
        return data;
    }
}
