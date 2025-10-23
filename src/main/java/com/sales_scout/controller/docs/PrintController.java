package com.sales_scout.controller.docs;

import com.sales_scout.dto.response.crm.wms.StorageContractResponseDto;
import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexe;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import com.sales_scout.repository.crm.wms.contract.StorageAnnexeRepository;
import com.sales_scout.repository.crm.wms.contract.StorageContractRepository;
import com.sales_scout.repository.crm.wms.credit_note.StorageCreditNoteRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import com.sales_scout.repository.crm.wms.offer.StorageOfferRepository;
import com.sales_scout.service.docs.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/print")
public class PrintController {

    private final DocxService docxService;
    private final StorageOfferRepository storageOfferRepository;
    private final StorageContractRepository storageContractRepository;
    private final ContractDocService contractDocService;
    private final OfferDocService offerDocService;
    private final ContractAnnexDocService contractAnnexDocService;
    private final StorageInvoiceRepository storageInvoiceRepository;
    private final InvoiceDocService invoiceDocService;
    private final StorageAnnexeRepository storageAnnexeRepository;
    private final StorageCreditNoteRepository storageCreditNoteRepository;
    private final StorageCreditNoteDocService creditNoteDocService;
    public PrintController(DocxService docxService, StorageOfferRepository storageOfferRepository, StorageContractRepository storageContractRepository, ContractDocService contractDocService, OfferDocService offerDocService, ContractAnnexDocService contractAnnexDocService, StorageInvoiceRepository storageInvoiceRepository, InvoiceDocService invoiceDocService, StorageAnnexeRepository storageAnnexeRepository, StorageCreditNoteRepository storageCreditNoteRepository, StorageCreditNoteDocService creditNoteDocService) {
        this.docxService = docxService;
        this.storageOfferRepository = storageOfferRepository;
        this.storageContractRepository = storageContractRepository;
        this.contractDocService = contractDocService;
        this.offerDocService = offerDocService;
        this.contractAnnexDocService = contractAnnexDocService;
        this.storageInvoiceRepository = storageInvoiceRepository;
        this.invoiceDocService = invoiceDocService;
        this.storageAnnexeRepository = storageAnnexeRepository;
        this.storageCreditNoteRepository = storageCreditNoteRepository;
        this.creditNoteDocService = creditNoteDocService;
    }

    @GetMapping("/generate-contract/{contract_id}")
    public ResponseEntity<byte[]> generateDocx(@PathVariable Long contract_id) throws IOException {
        // Replace placeholders in paragraphs
        Optional<StorageContract> storageContract = storageContractRepository.findById(contract_id);
        if (storageContract.isPresent()){
            try {
                byte[] docx = contractDocService.generateContractDocx(storageContract.get());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "Offre_entreposage_" + contract_id + ".docx");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(docx);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body(null);
            }
        }else {
            return ResponseEntity.internalServerError().body(null);
        }

    }

    /**
     *
     * @param offerId
     * @return
     */
    @GetMapping("/offer/{offerId}")
    public ResponseEntity<byte[]> generateOfferDoc(@PathVariable UUID offerId) {
        StorageOffer offer = storageOfferRepository.findByRefAndDeletedAtIsNull(offerId)
                .orElseThrow(() -> new RuntimeException("Storage Offer not found"));

        try {
            byte[] document = offerDocService.generateOfferDoc(offer);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Offre_entreposage_" + offerId + ".docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     *
     * @param contractAnnexeId
     * @return
     */
    @GetMapping("/contract-annexe/{contractAnnexeId}")
    public ResponseEntity<byte[]> generateOfferDoc(@PathVariable Long contractAnnexeId) {
        StorageAnnexe storageAnnexe = storageAnnexeRepository.findById(contractAnnexeId)
                .orElseThrow(() -> new RuntimeException("Storage annexe not found"));

        try {
            byte[] document = contractAnnexDocService.generateAnnexDocx(storageAnnexe);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "annexe_contract" + contractAnnexeId + ".docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


    /**
     *
     * @param creditNoteId
     * @return
     */
    @GetMapping("/credit-note/{creditNoteId}")
    public ResponseEntity<byte[]> generateCreditNoteDoc(@PathVariable Long creditNoteId) {
        StorageCreditNote creditNote = storageCreditNoteRepository.findById(creditNoteId)
                .orElseThrow(() -> new RuntimeException("Storage credit note not found"));

        try {
            byte[] document = creditNoteDocService.generateCreditNoteDoc(creditNote);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "credit_note" + creditNote.getNumber() + ".docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     *
     * @param invoiceId
     * @return
     */
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<byte[]> generateInvoiceDoc(@PathVariable Long invoiceId) {
        StorageInvoice storageInvoice = storageInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Storage Offer not found"));

        try {
            byte[] document = invoiceDocService.generateInvoiceDoc(storageInvoice);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "storage_invoice" + invoiceId + ".docx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
