package com.sales_scout.mapper.wms;

import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.crm.wms.delivery_note.StorageDeliveryNoteStorageContractStockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.invoice.InvoicePaymentRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceNoteStorageContractStockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.invoice.StoragePaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StorageInvoiceMapper {
    private final StorageContractMapper storageContractMapper;
    private final ProvisionMapper provisionMapper;
    private final StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository;
    private final UserMapper userMapper;
    private final StorageDeliveryNoteMapper storageDeliveryNoteMapper;
    private final StorageInvoiceNoteStorageContractStockedItemProvisionRepository storageInvoiceNoteStorageContractStockedItemProvisionRepository;
    private final StoragePaymentRepository storagePaymentRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;
    public StorageInvoiceMapper(StorageContractMapper storageContractMapper, ProvisionMapper provisionMapper,
                                StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository,
                                UserMapper userMapper, StorageDeliveryNoteMapper storageDeliveryNoteMapper, StorageInvoiceNoteStorageContractStockedItemProvisionRepository storageInvoiceNoteStorageContractStockedItemProvisionRepository, StoragePaymentRepository storagePaymentRepository, InvoicePaymentRepository invoicePaymentRepository) {
        this.storageContractMapper = storageContractMapper;
        this.provisionMapper = provisionMapper;
        this.storageDeliveryNoteStorageContractStockedItemProvisionRepository = storageDeliveryNoteStorageContractStockedItemProvisionRepository;
        this.userMapper = userMapper;
        this.storageDeliveryNoteMapper = storageDeliveryNoteMapper;
        this.storageInvoiceNoteStorageContractStockedItemProvisionRepository = storageInvoiceNoteStorageContractStockedItemProvisionRepository;
        this.storagePaymentRepository = storagePaymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    public StorageInvoiceResponseDto toResponse(StorageInvoice invoice){

        StorageInvoiceResponseDto storageInvoiceResponseDto = StorageInvoiceResponseDto.builder()
                .id(invoice.getId())
                .number(invoice.getNumber())
                .dueDate(invoice.getDueDate())
                .invoiceDate(invoice.getInvoiceDate())
                .storageDeliveryNote(storageDeliveryNoteMapper.toResponse(invoice.getStorageDeliveryNote()))
                .status(invoice.getStatus())
                .stockedItemResponseDtos(
                        storageInvoiceNoteStorageContractStockedItemProvisionRepository.findByStorageInvoiceId(invoice.getId())
                                .stream()
                                .filter(itrm -> itrm.getStorageContractStockedItem() != null && itrm.getStorageContractStockedItem().getStockedItem() != null)
                                .map(itrm -> StorageDeliveryNoteItemQuantity.builder()
                                        .id(itrm.getId())
                                        .provisionResponseDto(ProvisionMapper.toDto(itrm.getStockedItemProvision()))
                                        .quantity(itrm.getQuantity())
                                        .stockedItemResponseDto(StockedItemMapper.toResponseDto(itrm.getStorageContractStockedItem().getStockedItem()))
                                        .build())
                                .collect(Collectors.toSet())
                )
                .storageContract(this.storageContractMapper.toResponseDto(invoice.getStorageContract()))
                .build();


        storageInvoiceResponseDto.setUnloadingTypeResponseDtos(
              invoice.getStorageInvoiceStorageContractUnloadingTypes().stream().map(storageDeliveryNoteStorageContractUnloadingType -> {
                  UnloadingType unloadingType = storageDeliveryNoteStorageContractUnloadingType.getStorageContractUnloadingType().getUnloadingType();
                  return StorageDeliveryNoteUnloadQuantity.builder()
                          .id(storageDeliveryNoteStorageContractUnloadingType.getId())
                          .unloadingTypeResponseDto(UnloadingTypeResponseDto.builder()
                                  .name(unloadingType.getName())
                                  .salesPrice(storageDeliveryNoteStorageContractUnloadingType.getStorageContractUnloadingType().getSalesPrice())
                                  .unitOfMeasurement(unloadingType.getUnitOfMeasurement())
                                  .build())
                          .quantity(storageDeliveryNoteStorageContractUnloadingType.getQuantity())
                          .storageDeliveryNoteUnloadId(storageDeliveryNoteStorageContractUnloadingType.getId())
                          .build();
              }).collect(Collectors.toSet())
        );

        storageInvoiceResponseDto.setRequirementResponseDtos(invoice.getStorageInvoiceStorageContractRequirementList().stream().map(storageDeliveryNoteStorageContractRequirement -> {
            Requirement requirement = storageDeliveryNoteStorageContractRequirement.getStorageContractRequirement().getRequirement();
            return StorageDeliveryNoteRequirementQuantity.builder()
                    .requirementResponseDto(StorageRequirementResponseDto.builder()
                            .name(requirement.getName())
                            .salesPrice(storageDeliveryNoteStorageContractRequirement.getStorageContractRequirement().getSalesPrice())
                            .unitOfMeasurement(requirement.getUnitOfMeasurement())
                            .build())
                    .quantity(storageDeliveryNoteStorageContractRequirement.getQuantity())
                    .id(storageDeliveryNoteStorageContractRequirement.getId())
                    .build();
        }).collect(Collectors.toSet()));

        storageInvoiceResponseDto.setTotalHt(invoice.getTotalHt());
        storageInvoiceResponseDto.setTva(invoice.getTva());
        storageInvoiceResponseDto.setTotalTtc(invoice.getTotalTtc());
        Set<StorageInvoicePaymentRequestDto> storageInvoicePayment = invoicePaymentRepository.findByStorageInvoiceId(invoice.getId()).stream()
                .map(invoicePayment -> {
                  return   StorageInvoicePaymentRequestDto.builder()
                          .id(invoicePayment.getPayment().getId())
                          .amount(invoicePayment.getPayment().getAmount())
                          .ref(invoicePayment.getPayment().getRef())
                          .paymentMethod(invoicePayment.getPayment().getPaymentMethod())
                          .createdAt(invoicePayment.getCreatedAt())
                          .receptionDate(invoicePayment.getPayment().getReceptionDate())
                          .validationDate(invoicePayment.getPayment().getValidationDate())
                          .validationStatus(invoicePayment.getPayment().isValidationStatus())
                          .build();
                }).collect(Collectors.toSet());
        storageInvoiceResponseDto.setSendDate(invoice.getSendDate());
        storageInvoiceResponseDto.setSendStatus(invoice.getSendStatus());
        storageInvoiceResponseDto.setReturnDate(invoice.getReturnDate());
        storageInvoiceResponseDto.setReturnStatus(invoice.getReturnStatus());
        storageInvoiceResponseDto.setStorageInvoicePaymentRequestDtos(storageInvoicePayment);
        storageInvoiceResponseDto.setCreatedAt(invoice.getCreatedAt());
        storageInvoiceResponseDto.setCreatedBy(invoice.getCreatedBy() != null ? userMapper.fromEntity(invoice.getCreatedBy()): null);
        storageInvoiceResponseDto.setUpdatedAt(invoice.getUpdatedAt());
        storageInvoiceResponseDto.setUpdatedBy(invoice.getUpdatedBy() != null ? userMapper.fromEntity(invoice.getUpdatedBy()): null);

        return storageInvoiceResponseDto;
    }

    public StorageInvoiceResponseDto toLightDto(StorageInvoice invoice) {
        return StorageInvoiceResponseDto.builder()
                .id(invoice.getId())
                .number(invoice.getNumber())
                .build();
    }
}
