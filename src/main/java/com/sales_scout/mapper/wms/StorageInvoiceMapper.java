package com.sales_scout.mapper.wms;

import com.sales_scout.dto.response.crm.wms.*;
import com.sales_scout.entity.crm.wms.Requirement;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.entity.crm.wms.deliveryNote.StorageDeliveryNote;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.crm.wms.StorageDeliveryNoteStorageContractStockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceNoteStorageContractStockedItemProvisionRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StorageInvoiceMapper {
    private final StorageContractMapper storageContractMapper;
    private final ProvisionMapper provisionMapper;
    private final StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository;
    private final UserMapper userMapper;
    private final StorageDeliveryNoteMapper storageDeliveryNoteMapper;
    private final StorageInvoiceNoteStorageContractStockedItemProvisionRepository storageInvoiceNoteStorageContractStockedItemProvisionRepository;
    public StorageInvoiceMapper(StorageContractMapper storageContractMapper, ProvisionMapper provisionMapper,
                                StorageDeliveryNoteStorageContractStockedItemProvisionRepository storageDeliveryNoteStorageContractStockedItemProvisionRepository,
                                UserMapper userMapper, StorageDeliveryNoteMapper storageDeliveryNoteMapper, StorageInvoiceNoteStorageContractStockedItemProvisionRepository storageInvoiceNoteStorageContractStockedItemProvisionRepository) {
        this.storageContractMapper = storageContractMapper;
        this.provisionMapper = provisionMapper;
        this.storageDeliveryNoteStorageContractStockedItemProvisionRepository = storageDeliveryNoteStorageContractStockedItemProvisionRepository;
        this.userMapper = userMapper;
        this.storageDeliveryNoteMapper = storageDeliveryNoteMapper;
        this.storageInvoiceNoteStorageContractStockedItemProvisionRepository = storageInvoiceNoteStorageContractStockedItemProvisionRepository;
    }

    public StorageInvoiceResponseDto toResponse(StorageInvoice invoice){

        StorageInvoiceResponseDto storageInvoiceResponseDto = StorageInvoiceResponseDto.builder()
                .id(invoice.getId())
                .number(invoice.getNumber())
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

        storageInvoiceResponseDto.setCreatedAt(invoice.getCreatedAt());
        storageInvoiceResponseDto.setCreatedBy(invoice.getCreatedBy() != null ? userMapper.fromEntity(invoice.getCreatedBy()): null);
        storageInvoiceResponseDto.setUpdatedAt(invoice.getUpdatedAt());
        storageInvoiceResponseDto.setUpdatedBy(invoice.getUpdatedBy() != null ? userMapper.fromEntity(invoice.getUpdatedBy()): null);

        return storageInvoiceResponseDto;
    }
}
