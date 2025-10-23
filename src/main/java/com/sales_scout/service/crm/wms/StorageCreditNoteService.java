package com.sales_scout.service.crm.wms;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.CreateStorageCreditNoteRequest;
import com.sales_scout.dto.request.update.StorageCreditNoteUpdateRequest;
import com.sales_scout.dto.response.crm.wms.StorageCreditNoteResponseDto;
import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
import com.sales_scout.entity.crm.wms.assets.StorageCreditNoteStatus;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageCreditNoteMapper;
import com.sales_scout.repository.crm.wms.credit_note.StorageCreditNoteRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageCreditNoteService {
    private final StorageCreditNoteRepository storageCreditNoteRepository;
    private final StorageInvoiceRepository storageInvoiceRepository;
    private final StorageCreditNoteMapper storageCreditNoteMapper;

    /**
     * This function allows to create new Storage Credit note by invoice id
     * @return {StorageCreditNoteResponseDto} the created storage credit note
     * @throws Exception in case of serve error
     * @throws ResourceNotFoundException in case of storage invoice note found
     */
    public StorageCreditNoteResponseDto createStorageCreditNote(CreateStorageCreditNoteRequest creditNote) throws Exception , ResourceNotFoundException {
        StorageInvoice storageInvoice = storageInvoiceRepository.findById(creditNote.getInvoiceId())
                .orElseThrow(()-> new ResourceNotFoundException("Storage Invoice not found with id "+ creditNote.getInvoiceId(), "", ""));
        StorageCreditNote storageCreditNote = StorageCreditNote.builder()
                .storageInvoice(storageInvoice)
                .totalHt(creditNote.getAmountHt())
                .totalTtc(creditNote.getAmountHt() * 1.2) // calculate the amount TTC
                .tva((creditNote.getAmountHt() * 1.2) - creditNote.getAmountHt()  )// Calculate the amount of TVA
                .number("AEN-" + String.format("%04d", storageCreditNoteRepository.count() + 1) + "/" + LocalDateTime.now().getYear())
                .company(storageInvoice.getStorageContract().getCompany())
                .status(StorageCreditNoteStatus.builder().id(1L).build())
                .build();
        storageCreditNote.setCreatedBy(SecurityUtils.getCurrentUser());
        storageCreditNote.setUpdatedBy(SecurityUtils.getCurrentUser());
        return storageCreditNoteMapper.toResponseDto(storageCreditNoteRepository.save(storageCreditNote));
    }

    /**
     *
     * @param creditNote
     * @return
     * @throws Exception
     * @throws ResourceNotFoundException
     */
    public StorageCreditNoteResponseDto updateStorageCreditNote(StorageCreditNoteUpdateRequest creditNote) throws Exception , ResourceNotFoundException {
//        StorageInvoice storageInvoice = storageInvoiceRepository.findById(creditNote.getInvoiceId())
//                .orElseThrow(()-> new ResourceNotFoundException("Storage Invoice not found with id "+ creditNote.getInvoiceId(), "", ""));
        StorageCreditNote storageCreditNote = this.storageCreditNoteRepository.findById(creditNote.getId())
                .orElseThrow(() -> new ResourceNotFoundException("","",""));

        storageCreditNote.setTotalHt(creditNote.getTotalHt());
        storageCreditNote.setTotalTtc(creditNote.getTotalHt() * 1.2); // calculate the amount TTC
        storageCreditNote.setTva((creditNote.getTotalHt() * 1.2) - creditNote.getTotalHt());// Calculate the amount of TVA
        storageCreditNote.setSendDate(creditNote.getSendDate());
        storageCreditNote.setSendStatus(creditNote.getSendStatus());
        storageCreditNote.setReturnDate(creditNote.getReturnDate());
        storageCreditNote.setReturnStatus(creditNote.getReturnStatus());
        storageCreditNote.setUpdatedBy(SecurityUtils.getCurrentUser());
        return storageCreditNoteMapper.toResponseDto(storageCreditNoteRepository.save(storageCreditNote));
    }



    /**
     *
     * @param companyId
     * @return
     */
    public List<StorageCreditNoteResponseDto> getAllStorageCreditNote(Long companyId) {
        return  this.storageCreditNoteRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream().map(storageCreditNoteMapper::toResponseDto).collect(Collectors.toList());
    }

    /**
     *
     * @param creditNoteId
     * @return
     */
    public StorageCreditNoteResponseDto getStorageCreditNoteById(Long creditNoteId) {
        StorageCreditNote storageCreditNote = this.storageCreditNoteRepository.findById(creditNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("","",""));
        return storageCreditNoteMapper.toResponseDto(storageCreditNote);
    }
}
