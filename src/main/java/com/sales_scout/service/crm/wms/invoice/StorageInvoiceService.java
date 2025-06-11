package com.sales_scout.service.crm.wms.invoice;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.response.crm.wms.StorageInvoiceResponseDto;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.invoice.*;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageInvoiceMapper;
import com.sales_scout.repository.crm.wms.delivery_note.StorageDeliveryNoteRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageInvoiceService {
    private final StorageInvoiceRepository storageInvoiceRepository;
    private final StorageInvoiceMapper storageInvoiceMapper;
    private final StorageDeliveryNoteRepository storageDeliveryNoteRepository;

    public StorageInvoiceService(StorageInvoiceRepository storageInvoiceRepository, StorageInvoiceMapper storageInvoiceMapper, StorageDeliveryNoteRepository storageDeliveryNoteRepository) {
        this.storageInvoiceRepository = storageInvoiceRepository;
        this.storageInvoiceMapper = storageInvoiceMapper;
        this.storageDeliveryNoteRepository = storageDeliveryNoteRepository;
    }

    /**
     * this function allows to get all storage invoice by company id
     * @param companyId the company id
     * @return {List<StorageInvoiceResponseDto>} list storage invoice Dtos
     * @throws ResourceNotFoundException in case of empty
     */
    public List<StorageInvoiceResponseDto> getAllStorageInvoiceByCompanyId(Long companyId) throws ResourceNotFoundException {
       return this.storageInvoiceRepository.findAllByStorageContract_companyIdAndDeletedAtIsNull(companyId).stream()
               .map(storageInvoiceMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * this function allows to create Storage invoice from delivery note
     * @param storageDeliveryNoteId storage delivery note id
     * @return {StorageInvoiceResponseDto}
     * @throws ResourceNotFoundException in case of delivery note not found
     */
    @Transactional
    public StorageInvoiceResponseDto createStorageInvoiceFromStorageDeliveryNote(Long storageDeliveryNoteId)
            throws ResourceNotFoundException {

        StorageDeliveryNote storageDeliveryNote = storageDeliveryNoteRepository.findById(storageDeliveryNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("StorageDeliveryNote", "id", storageDeliveryNoteId.toString()));

        StorageInvoice storageInvoice = StorageInvoice.builder()
                .storageContract(storageDeliveryNote.getStorageContract())
                .storageDeliveryNote(storageDeliveryNote)
                .status(StorageInvoiceStatus.builder().id(1L).build())
                .ref(UUID.randomUUID())
                .number("FEN-" + String.format("%04d", storageInvoiceRepository.count() + 1) + "/" + LocalDateTime.now().getYear())
                .build();

        StorageInvoice savedStorageInvoice = storageInvoiceRepository.save(storageInvoice);

        double totalHt = 0.0;

        // Unloading Types
        List<StorageInvoiceStorageContractUnloadingType> unloadingTypes = new ArrayList<>();
        for (var unloadingType : storageDeliveryNote.getStorageDeliveryNoteStorageContractUnloadingTypes()) {
            double unitPrice = Optional.ofNullable(unloadingType.getStorageContractUnloadingType().getSalesPrice()).orElse(0.0);
            double lineTotal = unloadingType.getQuantity() * unitPrice;
            totalHt += lineTotal;

            unloadingTypes.add(StorageInvoiceStorageContractUnloadingType.builder()
                    .storageContractUnloadingType(unloadingType.getStorageContractUnloadingType())
                    .storageInvoice(savedStorageInvoice)
                    .quantity(unloadingType.getQuantity())
                    .ref(UUID.randomUUID())
                    .totalHt(lineTotal)
                    .build());
        }

        // Stocked Items
        List<StorageInvoiceStorageContractStockedItemProvision> stockedItems = new ArrayList<>();
        for (var stockedItem : storageDeliveryNote.getStorageDeliveryNoteStorageContractStockedItems()) {
            double unitPrice = Optional.ofNullable(stockedItem.getStockedItemProvision().getSalesPrice()).orElse(0.0);
            double lineTotal = stockedItem.getQuantity() * unitPrice;
            totalHt += lineTotal;

            stockedItems.add(StorageInvoiceStorageContractStockedItemProvision.builder()
                    .storageInvoice(savedStorageInvoice)
                    .storageContractStockedItem(stockedItem.getStorageContractStockedItem())
                    .stockedItemProvision(stockedItem.getStockedItemProvision())
                    .quantity(stockedItem.getQuantity())
                    .ref(UUID.randomUUID())
                    .totalHt(lineTotal)
                    .build());
        }

        // Requirements
        List<StorageInvoiceStorageContractRequirement> requirements = new ArrayList<>();
        for (var requirement : storageDeliveryNote.getStorageDeliveryNoteStorageContractRequirements()) {
            double unitPrice = Optional.ofNullable(requirement.getStorageContractRequirement().getSalesPrice()).orElse(0.0);
            double lineTotal = requirement.getQuantity() * unitPrice;
            totalHt += lineTotal;

            requirements.add(StorageInvoiceStorageContractRequirement.builder()
                    .storageInvoice(savedStorageInvoice)
                    .storageContractRequirement(requirement.getStorageContractRequirement())
                    .quantity(requirement.getQuantity())
                    .totalHt(lineTotal)
                    .build());
        }

        savedStorageInvoice.setStorageInvoiceStorageContractUnloadingTypes(unloadingTypes);
        savedStorageInvoice.setStorageInvoiceStorageContractStockedItemProvisions(stockedItems);
        savedStorageInvoice.setStorageInvoiceStorageContractRequirementList(requirements);
        savedStorageInvoice.setCreatedBy(SecurityUtils.getCurrentUser());

        savedStorageInvoice.setTotalHt(totalHt);
        savedStorageInvoice.setTotalTtc(totalHt * 1.2);
        savedStorageInvoice.setTva(savedStorageInvoice.getTotalTtc() - totalHt);

        return storageInvoiceMapper.toResponse(storageInvoiceRepository.save(savedStorageInvoice));
    }



    /**
     * this function allows to get invoice by id and deleted at is null
     * @param invoiceId the invoice id
     * @return StorageInvoiceResponseDto
     * @throws ResourceNotFoundException if storage invoice not found
     */
    public StorageInvoiceResponseDto getStorageInvoiceByCompanyId(Long invoiceId)throws ResourceNotFoundException {
        Optional<StorageInvoice> storageInvoice = storageInvoiceRepository.findByIdAndDeletedAtIsNull(invoiceId);
        if (storageInvoice.isEmpty()){
            throw new ResourceNotFoundException("invoice not found with id "+ invoiceId.toString(),"","");
        }
        return storageInvoiceMapper.toResponse(storageInvoice.get());
    }
}

