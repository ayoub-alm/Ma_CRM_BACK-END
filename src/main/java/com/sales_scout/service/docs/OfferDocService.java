package com.sales_scout.service.docs;

import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.entity.crm.wms.offer.StorageOfferRequirement;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStockedItem;
import com.sales_scout.entity.crm.wms.offer.StorageOfferUnloadType;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRequirementRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferStockedItemRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferUnloadTypeRepository;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OfferDocService {
    private final StorageOfferStockedItemRepository storageOfferStockedItemRepository;
    private final StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository;
    private final StorageOfferRequirementRepository storageOfferRequirementRepository;
    public OfferDocService(StorageOfferStockedItemRepository storageOfferStockedItemRepository, StorageOfferUnloadTypeRepository storageOfferUnloadTypeRepository, StorageOfferRequirementRepository storageOfferRequirementRepository) {
        this.storageOfferStockedItemRepository = storageOfferStockedItemRepository;
        this.storageOfferUnloadTypeRepository = storageOfferUnloadTypeRepository;
        this.storageOfferRequirementRepository = storageOfferRequirementRepository;
    }

    public byte[] generateOfferDoc(StorageOffer offer) throws IOException, IOException {
        InputStream inputStream = new ClassPathResource("templates/offer.docx").getInputStream();
        XWPFDocument document = new XWPFDocument(inputStream);

        Map<String, String> placeholders = preparePlaceholders(offer);

        document.getParagraphs().forEach(paragraph -> replaceText(paragraph, placeholders));
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceText(paragraph, placeholders);
        }

        replacePlaceholdersInDocument(document,placeholders);

        List<XWPFTable> tables = document.getTables();
        if (tables.size() >= 4) {
            fillTableWithFormatting(tables.get(0), extractGeneralData(offer), true);
//            fillTableWithFormatting(tables.get(1), extractGeneralData2(offer), false);
            fillTable(tables.get(1), extractStockedItemsData(offer));
            fillTable(tables.get(2), extractUnloadingTypeDataForOffer(offer));
            fillTable(tables.get(3), extractOperationsData(offer));
            fillTable(tables.get(4), extractRequirementsData(offer));
        } else {
            throw new IllegalStateException("DOCX template does not contain enough tables.");
        }

        this.applyWhiteHeadersExceptFirstTable(document);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        document.close();
        return outputStream.toByteArray();
    }

    private List<List<String>> extractGeneralData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();

        data.add(Arrays.asList("Référence :", offer.getNumber() != null ? offer.getNumber() : "N/A"));
        data.add(Arrays.asList("Date :", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        data.add(Arrays.asList("Fin de validité :", LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        data.add(Arrays.asList("Commercial(e) :", offer.getCreatedBy() != null ? offer.getCreatedBy().getName() : "N/A"));
        data.add(Arrays.asList("Email :", offer.getCreatedBy() != null ? offer.getCreatedBy().getEmail() : "N/A"));
        data.add(Arrays.asList("Entreprise :", offer.getCustomer() != null ? offer.getCustomer().getName() : "N/A"));
        data.add(Arrays.asList("Type de produits :", offer.getProductType() != null ? offer.getProductType() : "N/A"));
        data.add(Arrays.asList("Durée de stockage :", offer.getDuration() != null ? offer.getDuration() + " Mois" : "N/A"));
        return data;
    }


    private Map<String, String> preparePlaceholders(StorageOffer offer) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("Référence", offer.getNumber() != null ? offer.getNumber() : "N/A");
        placeholders.put("Date_offre", offer.getCreatedAt() != null ? offer.getCreatedAt().toLocalDate().toString() : "N/A");
        placeholders.put("Fin_de_validite",LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        placeholders.put("Cree_par", offer.getInterlocutor() != null ? offer.getInterlocutor().getFullName() : "N/A");
        placeholders.put("Email", offer.getCreatedBy() != null ? offer.getCreatedBy().getEmail() : "N/A");
        placeholders.put("Entreprise", offer.getCustomer() != null ? offer.getCustomer().getName() : "N/A");
        placeholders.put("Type_de_produit", offer.getProductType() != null ? offer.getProductType() : "N/A");
        placeholders.put("product_type", offer.getProductType() != null ? offer.getProductType() : "N/A");
        placeholders.put("Type_de_product_line_tree", offer.getProductType() != null ? offer.getProductType() : "N/A");
        placeholders.put("Duree_de_stockage", offer.getDuration() != null ? String.valueOf(offer.getDuration()) : "0");
        placeholders.put("Duree_de_stockage_too", offer.getDuration() != null ? String.valueOf(offer.getDuration()) : "0");
        placeholders.put("Duree_de_stockage_tree", offer.getDuration() != null ? String.valueOf(offer.getDuration()) : "0");
        placeholders.put("Raison_de_stockage", offer.getStorageReason() != StorageReasonEnum.OUTSOURCING ? "Débord" : "Extérnalisation");
        placeholders.put("storage_reason", offer.getStorageReason() != StorageReasonEnum.OUTSOURCING ? "Débord" : "Extérnalisation");
        placeholders.put("SKU_value", offer.getNumberOfSku() > 0 ? String.valueOf(offer.getNumberOfSku()) : "0");
        placeholders.put("Livre", offer.getLiverStatus() != LivreEnum.CLOSE ? "Ouvert" : "Fermé");

        placeholders.put("Facturation_minimale_assuree", String.format("%.2f", offer.getMinimumBillingGuaranteedFixed() != null ?
                offer.getMinimumBillingGuaranteedFixed() : offer.getMinimumBillingGuaranteed()));

        placeholders.put("Emplacements_palettes_reserves", offer.getNumberOfReservedPlaces().toString());
        placeholders.put("Valeur_de_frais_de_gestion", offer.getManagementFees().toString());
        placeholders.put("devise", offer.getDevise());
        if (offer.getNote() != null){
            placeholders.put("Notes", !Objects.equals(offer.getNote(), "") ? offer.getNote():"");
        }else{
            placeholders.put("Notes","");
        }
        return placeholders;
    }


    private void replaceText(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    String replacement = entry.getValue() != null ? entry.getValue() : "N/A";  // ✅ Replace missing values with "N/A"
                    text = text.replace("{{" + entry.getKey() + "}}", replacement);
                }
                run.setText(text, 0);
            }
        }
    }


    private List<List<String>> extractStockedItemsData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        List<StorageOfferStockedItem> stockedItems = storageOfferStockedItemRepository.findAllByStorageOfferId(offer.getId());

        for (StorageOfferStockedItem item : stockedItems) {
            String support = item.getStockedItem().getSupport() != null ? item.getStockedItem().getSupport().getName() : "N/A";
            String weight = item.getStockedItem().getWeight() != null ? item.getStockedItem().getWeight().toString() : "0";
            String dimensions = (item.getStockedItem().getDimension() != null) ?
                    Math.round(item.getStockedItem().getDimension().getLength()) + "x" +
                            Math.round(item.getStockedItem().getDimension().getWidth()) + "x" +
                            Math.round(item.getStockedItem().getDimension().getHeight())
                    : "N/A";

            data.add(Arrays.asList(support, weight, dimensions));
        }
        return data;
    }

    private List<List<String>> extractUnloadingTypeDataForOffer(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        List<StorageOfferUnloadType> storageOfferUnloadTypes = storageOfferUnloadTypeRepository.findAllByStorageOfferId(offer.getId());
        storageOfferUnloadTypes.forEach(storageOfferUnloadType -> {
            data.add(Arrays.asList(
                    storageOfferUnloadType.getUnloadingType().getName(),
                    storageOfferUnloadType.getUnloadingType().getUnitOfMeasurement(),
                    storageOfferUnloadType.getSalesPrice().toString()));
        });
        return data;
    }

    private List<List<String>> extractOperationsData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        List<StorageOfferStockedItem> stockedItems = storageOfferStockedItemRepository.findAllByStorageOfferId(offer.getId());

        stockedItems.forEach(item -> {
            if (item.getStockedItem() != null && item.getStockedItem().getStockedItemProvisions() != null) {
                item.getStockedItem().getStockedItemProvisions().forEach(stockedItemProvision -> {
                    if (stockedItemProvision.getProvision() != null) {
                        String provisionName = stockedItemProvision.getProvision().getName() != null
                                ? stockedItemProvision.getProvision().getName()
                                : "N/A";

                        String supportName = (item.getStockedItem().getSupport() != null
                                && item.getStockedItem().getSupport().getName() != null)
                                ? item.getStockedItem().getSupport().getName()
                                : "N/A";

                        String unitOfMeasurement = stockedItemProvision.getProvision().getUnitOfMeasurement() != null
                                ? stockedItemProvision.getProvision().getUnitOfMeasurement()
                                : "N/A";

                        String salesPrice = "0.00";
                        Double price = stockedItemProvision.getSalesPrice();
                        if (price != null && price != 0.0) {
                            salesPrice = String.format("%.2f", price);
                        }

                        data.add(Arrays.asList(provisionName + " " + supportName, unitOfMeasurement, salesPrice));
                    }
                });
            }
        });

        return data;
    }

    private List<List<String>> extractRequirementsData(StorageOffer offer) {
        List<List<String>> data = new ArrayList<>();
        List<StorageOfferRequirement> storageOfferRequirements = storageOfferRequirementRepository.findAllByStorageOfferId(offer.getId());

        storageOfferRequirements.forEach(StorageOfferRequirement -> {
            data.add(Arrays.asList(StorageOfferRequirement.getRequirement().getName(),
                    StorageOfferRequirement.getRequirement().getUnitOfMeasurement(),
                    StorageOfferRequirement.getSalesPrice().toString()));
       });
       return data;
    }

    public void fillTable(XWPFTable table, List<List<String>> tableData) {
        while (table.getNumberOfRows() > 1) {
            table.removeRow(1);
        }

        for (List<String> rowData : tableData) {
            XWPFTableRow row = table.createRow();

            for (int i = 0; i < rowData.size(); i++) {
                XWPFTableCell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell();                  }

                XWPFParagraph paragraph = cell.getParagraphs().get(0);
                XWPFRun run = paragraph.createRun();
                run.setText(rowData.get(i) != null ? rowData.get(i) : "N/A");

                run.setFontSize(9);
            }
        }
    }



    private void fillTableWithFormatting(XWPFTable table, List<List<String>> tableData, boolean colorWhite) {
        while (table.getNumberOfRows() > 1) {
            table.removeRow(1);
        }

        for (List<String> rowData : tableData) {
            XWPFTableRow row = table.createRow();

            for (int i = 0; i < rowData.size(); i++) {
                XWPFTableCell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell();  // Ensure each cell exists before adding text
                }

                XWPFParagraph paragraph = cell.getParagraphs().get(0);
                XWPFRun run = paragraph.createRun();
                run.setText(rowData.get(i) != null ? rowData.get(i) : "N/A");

                // ✅ Set text color to white
                if (colorWhite) {
                    run.setColor("FFFFFF");
                } else {
                    run.setColor("000000");
                }
                run.setFontSize(11);
            }
        }
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


    private void applyWhiteHeadersExceptFirstTable(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();

        for (int i = 1; i < tables.size(); i++) {
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
