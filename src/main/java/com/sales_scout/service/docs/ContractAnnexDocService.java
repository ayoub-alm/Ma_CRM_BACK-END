//package com.sales_scout.service.docs;
//
//import com.sales_scout.entity.crm.wms.contract.*;
//import com.sales_scout.entity.crm.wms.offer.*;
//import com.sales_scout.entity.leads.Customer;
//import com.sales_scout.entity.leads.Interlocutor;
//import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
//import org.apache.commons.io.output.ByteArrayOutputStream;
//import org.apache.poi.xwpf.usermodel.*;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//@Service
//public class ContractAnnexDocService {
//    private final ContractStockedItemRepository contractStockedItemRepository;
//
//    public ContractAnnexDocService(ContractStockedItemRepository contractStockedItemRepository) {
//        this.contractStockedItemRepository = contractStockedItemRepository;
//    }
//
//    public byte[] generateAnnexDocx(StorageContract annexContract) throws IOException {
//        try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//            XWPFParagraph title = document.createParagraph();
//            title.setAlignment(ParagraphAlignment.CENTER);
//            XWPFRun run = title.createRun();
//            run.setBold(true);
//            run.setFontSize(16);
//            run.setText("ANNEXE AU CONTRAT DE PRESTATION LOGISTIQUE");
//
//            // Introduction
//            appendParagraph(document, "Cette annexe est liée au contrat principal numéro " +
//                    Optional.ofNullable(annexContract.getParentContract()).map(StorageContract::getNumber).orElse("N/A") +
//                    " conclu entre les parties.");
//
//            // Dates
//            appendParagraph(document, "Date de génération : " + LocalDate.now().format(formatter));
//            appendParagraph(document, "Date d'effet : " +
//                    Optional.ofNullable(annexContract.getStartDate()).map(formatter::format).orElse("N/A"));
//            appendParagraph(document, "Date d'échéance : " +
//                    Optional.ofNullable(annexContract.getEndDate()).map(formatter::format).orElse("N/A"));
//
//            // Client info
//            Customer customer = annexContract.getCustomer();
//            appendParagraph(document, "Client : " + customer.getName());
//            appendParagraph(document, "Siège : " + customer.getHeadOffice());
//
//            // Interlocutor
//            Interlocutor interlocutor = annexContract.getInterlocutor();
//            appendParagraph(document, "Représentant : " + interlocutor.getFullName() + " (" + interlocutor.getJobTitle().getName() + ")");
//
////            // Stocked Items
//            appendParagraph(document, "\nArticles stockés :");
//            for (StorageContractStockedItem item : contractStockedItemRepository.findAllByStorageContractId(annexContract.getId())) {
//                appendParagraph(document, "- " + item.getStockedItem().getUvc() + ", poids: " + item.getStockedItem().getWeight() + ", dimensions: " +
//                        item.getStockedItem().getDimension().getLength() + "x" +
//                        item.getStockedItem().getDimension().getWidth() + "x" +
//                        item.getStockedItem().getDimension().getHeight());
//            }
//
//            // Requirements
//            appendParagraph(document, "\nExigences spécifiques :");
//            for (StorageContractRequirement req : annexContract.getStorageContractRequirements()) {
//                appendParagraph(document, "- " + req.getRequirement().getName() + ", Prix init: " + req.getInitPrice() + ", Remise: " + req.getDiscountValue());
//            }
//
//            // Unloading Types
//            appendParagraph(document, "\nTypes de déchargement :");
//            for (StorageContractUnloadingType unload : annexContract.getStorageContractUnloadingTypes()) {
//                appendParagraph(document, "- " + unload.getUnloadingType().getName() + ", Prix: " + unload.getSalesPrice());
//            }
//
//            // Offer references
//            appendParagraph(document, "\nOffres liées :");
//            for (StorageOffer offer : annexContract.getStorageOffers()) {
//                appendParagraph(document, "- Offre n° " + offer.getNumber() + " (Date d'expiration : " +
//                        Optional.ofNullable(offer.getExpirationDate()).map(formatter::format).orElse("N/A") + ")");
//            }
//
//            // Final text
//            appendParagraph(document, "\nCette annexe fait partie intégrante du contrat principal et est soumise aux mêmes conditions.");
//            document.write(outputStream);
//            return outputStream.toByteArray();
//        }
//    }
//
//    private void appendParagraph(XWPFDocument doc, String text) {
//        XWPFParagraph para = doc.createParagraph();
//        XWPFRun run = para.createRun();
//        run.setText(text);
//    }
//}
package com.sales_scout.service.docs;

import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.contract.*;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.repository.crm.wms.contract.ContractRequirementRepository;
import com.sales_scout.repository.crm.wms.contract.ContractStockedItemRepository;
import com.sales_scout.repository.crm.wms.contract.ContractUnloadingTypeRepository;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ContractAnnexDocService {

    private final ContractStockedItemRepository contractStockedItemRepository;
    private final ContractUnloadingTypeRepository contractUnloadingTypeRepository;
    private final ContractRequirementRepository contractRequirementRepository;
    public ContractAnnexDocService(ContractStockedItemRepository contractStockedItemRepository, ContractUnloadingTypeRepository contractUnloadingTypeRepository, ContractRequirementRepository contractRequirementRepository) {
        this.contractStockedItemRepository = contractStockedItemRepository;
        this.contractUnloadingTypeRepository = contractUnloadingTypeRepository;
        this.contractRequirementRepository = contractRequirementRepository;
    }

    public byte[] generateAnnexDocx(StorageAnnexe storageAnnexe) throws IOException {
        try (
                InputStream inputStream = new ClassPathResource("templates/annexe.docx").getInputStream();
                XWPFDocument document = new XWPFDocument(inputStream);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Map<String, String> placeholders = preparePlaceholders(storageAnnexe);

            // Replace all paragraphs, headers, footers
            replacePlaceholdersInDocument(document, placeholders);

            // Fill tables
            List<XWPFTable> tables = document.getTables();
            if (tables.size() >= 3) {
                fillTable(tables.get(0), extractDepotage(storageAnnexe));
                fillTable(tables.get(1), extractLogisticsOperations(storageAnnexe));
                fillTable(tables.get(2), extractValueAddedServices(storageAnnexe));
            }

            this.applyWhiteHeaders(document);

            document.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private Map<String, String> preparePlaceholders(StorageAnnexe storageAnnexe) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Map<String, String> placeholders = new HashMap<>();
//        placeholders.put("company", Optional.ofNullable(contract.getCustomer()).map(Customer::getName).orElse("N/A"));
//        placeholders.put("RC", Optional.ofNullable(contract.getCompany()).map(Company::getRc).orElse("N/A"));
//        placeholders.put("IF", Optional.ofNullable(contract.getCompany()).map(Company::getIfm).orElse("N/A"));
//        placeholders.put("siege_social", Optional.ofNullable(contract.getCustomer()).map(Customer::getHeadOffice).orElse("N/A"));
//        placeholders.put("Representant", Optional.ofNullable(contract.getInterlocutor()).map(Interlocutor::getFullName).orElse("N/A"));
//        placeholders.put("Sa_qualite", Optional.ofNullable(contract.getInterlocutor()).map(Interlocutor::getJobTitle).orElse("N/A"));
//        placeholders.put("Type_de_produit", Optional.ofNullable(contract.getProductType()).orElse("N/A"));
//        placeholders.put("Nombre_de_SKU", Optional.of(contract.getNumberOfSku()).map(String::valueOf).orElse("0"));
//        placeholders.put("Duree_de_stockage", Optional.ofNullable(contract.getDuration()).map(String::valueOf).orElse("0"));
//        placeholders.put("Raison_de_stockage", Optional.ofNullable(contract.getStorageReason()).map(Enum::name).orElse("N/A"));
//        placeholders.put("Livre", Optional.ofNullable(contract.getLiverStatus()).map(Enum::name).orElse("N/A"));
//        placeholders.put("Delais_de_paiement", Optional.of(contract.getPaymentDeadline()).map(String::valueOf).orElse("N/A"));
//        placeholders.put("Date_effet", Optional.ofNullable(contract.getStartDate()).map(formatter::format).orElse("N/A"));
//        placeholders.put("Date_echeance", Optional.ofNullable(contract.getEndDate()).map(formatter::format).orElse("N/A"));
        placeholders.put("Date_generer", storageAnnexe.getCreatedAt().format(formatter));
//        placeholders.put("Facturation_minimale_assuree", String.valueOf(Optional.ofNullable(contract.getMinimumBillingGuaranteed()).orElse(0.0)));
//        placeholders.put("Emplacements_palettes_reserves", String.valueOf(Optional.ofNullable(contract.getNumberOfReservedPlaces()).orElse(0L)));
//        placeholders.put("Valeur_de_frais_de_gestion", String.valueOf(Optional.ofNullable(contract.getManagementFees()).orElse(0.0)));
        placeholders.put("company", Optional.ofNullable(storageAnnexe.getStorageContract().getCustomer().getName()).orElse("N/A"));
        placeholders.put("Notes", Optional.ofNullable(storageAnnexe.getStorageContract().getNote()).orElse("Aucune"));
        placeholders.put("parent_contract", Optional.ofNullable(storageAnnexe.getStorageContract().getNumber()).orElse("Aucune"));
        placeholders.put("company_seller", Optional.ofNullable(storageAnnexe.getStorageContract().getCompany().getName()).orElse("Aucune"));
        placeholders.put("ref", Optional.ofNullable(storageAnnexe.getNumber()).orElse("Aucune"));
        return placeholders;
    }

    private List<List<String>> extractDepotage(StorageAnnexe storageAnnexe) {
        List<List<String>> rows = new ArrayList<>();
        for (StorageAnnexeUnloadingType unloadingType : contractUnloadingTypeRepository.findAllByAnnexeId(storageAnnexe.getId())) {
            rows.add(List.of(
                    unloadingType.getUnloadingType().getName(),
                    unloadingType.getUnloadingType().getUnitOfMeasurement(),
                    String.format("%.2f", unloadingType.getSalesPrice())
            ));
        }
        return rows;
    }

    private List<List<String>> extractLogisticsOperations(StorageAnnexe storageAnnexe) {
        List<List<String>> rows = new ArrayList<>();
        for (StorageAnnexeStockedItem item :contractStockedItemRepository.findAllByAnnexeId(storageAnnexe.getId())) {
            item.getStockedItem().getStockedItemProvisions().forEach(prv -> {
                rows.add(List.of(
                        prv.getProvision().getName(),
                        prv.getProvision().getUnitOfMeasurement(),
                        String.format("%.2f", prv.getSalesPrice())
                ));
            });
        }
        return rows;
    }

    private List<List<String>> extractValueAddedServices(StorageAnnexe storageAnnexe) {
        List<List<String>> rows = new ArrayList<>();
        for (StorageAnnexeRequirement req : contractRequirementRepository.findByAnnexeId(storageAnnexe.getId())) {
            rows.add(List.of(
                    req.getRequirement().getName(),
                    req.getRequirement().getUnitOfMeasurement(),
                    String.format("%.2f", req.getSalesPrice())
            ));
        }
        return rows;
    }

    private void fillTable(XWPFTable table, List<List<String>> tableData) {
        while (table.getNumberOfRows() > 1) table.removeRow(1);
        for (List<String> rowData : tableData) {
            XWPFTableRow row = table.createRow();
            for (int i = 0; i < rowData.size(); i++) {
                XWPFTableCell cell = row.getCell(i) == null ? row.createCell() : row.getCell(i);
                XWPFRun run = cell.getParagraphs().get(0).createRun();
                run.setText(rowData.get(i));
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

    private void applyWhiteHeaders(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();

        for (XWPFTable table : tables) {
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
