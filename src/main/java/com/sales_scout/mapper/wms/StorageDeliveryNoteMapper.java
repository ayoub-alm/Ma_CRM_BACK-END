package com.sales_scout.mapper.wms;

import com.sales_scout.dto.response.crm.wms.*;

import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNoteUpdateRequest;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.crm.wms.delivery_note.DeliveryNoteUpdateRequestRepository;
import com.sales_scout.repository.crm.wms.delivery_note.StorageDeliveryNoteStorageContractStockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StorageDeliveryNoteMapper {
    private final StorageContractMapper storageContractMapper;
    private final ProvisionMapper provisionMapper;
    private final StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository;
    private final UserMapper userMapper;
    private final DeliveryNoteUpdateRequestRepository deliveryNoteUpdateRequestRepository;
    private final StorageInvoiceRepository storageInvoiceRepository;

    public StorageDeliveryNoteMapper(StorageContractMapper storageContractMapper, ProvisionMapper provisionMapper,
                                     StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository,
                                     UserMapper userMapper, DeliveryNoteUpdateRequestRepository deliveryNoteUpdateRequestRepository, StorageInvoiceRepository storageInvoiceRepository) {
        this.storageContractMapper = storageContractMapper;
        this.provisionMapper = provisionMapper;
        this.storageDeliveryNoteStorageContractStockedItemProvisionRepository = storageDeliveryNoteStorageContractStockedItemProvisionRepository;
        this.userMapper = userMapper;
        this.deliveryNoteUpdateRequestRepository = deliveryNoteUpdateRequestRepository;
        this.storageInvoiceRepository = storageInvoiceRepository;
    }

    public StorageDeliveryNoteResponseDto toResponse(StorageDeliveryNote storageDeliveryNote){

        StorageDeliveryNoteResponseDto storageDeliveryNoteResponseDto = StorageDeliveryNoteResponseDto.builder()
                .id(storageDeliveryNote.getId())
                .number(storageDeliveryNote.getNumber())
                .status(storageDeliveryNote.getStatus())
                .stockedItemResponseDtos(
                        storageDeliveryNoteStorageContractStockedItemProvisionRepository.findByStorageDeliveryNoteId(storageDeliveryNote.getId())
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
                .storageContract(this.storageContractMapper.toResponseDto(storageDeliveryNote.getStorageContract()))
                .build();

       // set unloading types
        storageDeliveryNoteResponseDto.setUnloadingTypeResponseDtos(
              storageDeliveryNote.getStorageDeliveryNoteStorageContractUnloadingTypes().stream().map(storageDeliveryNoteStorageContractUnloadingType -> {
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
        // set requirements
        storageDeliveryNoteResponseDto.setRequirementResponseDtos(storageDeliveryNote.getStorageDeliveryNoteStorageContractRequirements().stream().map(storageDeliveryNoteStorageContractRequirement -> {
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
        // set storage invoices
        storageDeliveryNoteResponseDto.setStorageInvoiceResponseDtos(
                this.storageInvoiceRepository.findByStorageDeliveryNoteId(storageDeliveryNote.getId()).stream()
                        .map(invoice -> {
                            return StorageInvoiceResponseDto.builder()
                                    .id(invoice.getId())
                                    .number(invoice.getNumber())
                                    .build();
                        })
                        .collect(Collectors.toSet())
        );
        storageDeliveryNoteResponseDto.setCreatedAt(storageDeliveryNote.getCreatedAt());
        storageDeliveryNoteResponseDto.setCreatedBy(storageDeliveryNote.getCreatedBy() != null ? userMapper.fromEntity(storageDeliveryNote.getCreatedBy()): null);
        storageDeliveryNoteResponseDto.setUpdatedAt(storageDeliveryNote.getUpdatedAt());
        storageDeliveryNoteResponseDto.setUpdatedBy(storageDeliveryNote.getUpdatedBy() != null ? userMapper.fromEntity(storageDeliveryNote.getUpdatedBy()): null);
        // set update requests
        storageDeliveryNoteResponseDto.setStorageDeliveryNoteUpdateRequests(
                deliveryNoteUpdateRequestRepository.findByStorageDeliveryNoteId(storageDeliveryNote.getId()).stream()
                        .map(storageDeliveryNoteUpdateRequest -> {
                            StorageDeliveryNoteUpdateRequest sdn = StorageDeliveryNoteUpdateRequest.builder()
                                    .id(storageDeliveryNoteUpdateRequest.getId())
                                    .note(storageDeliveryNoteUpdateRequest.getNote())
                                    .status(storageDeliveryNoteUpdateRequest.getStatus())
                                    .build();
                            sdn.setCreatedAt(storageDeliveryNoteUpdateRequest.getCreatedAt());
                            sdn.setCreatedAt(storageDeliveryNoteUpdateRequest.getCreatedAt());
                            sdn.setCreatedBy(storageDeliveryNoteUpdateRequest.getCreatedBy());
                            sdn.setUpdatedBy(storageDeliveryNoteUpdateRequest.getUpdatedBy());
                            return sdn;
                        }).collect(Collectors.toSet()));
        return storageDeliveryNoteResponseDto;
    }
}
