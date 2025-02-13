package com.sales_scout.mapper;

import com.sales_scout.dto.request.create.wms.StockedItemRequestDto;
import com.sales_scout.dto.response.crm.wms.StockedItemResponseDto;
import com.sales_scout.entity.crm.wms.*;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;

public class StockedItemMapper {

    // Map entity to response DTO
    public static StockedItemResponseDto toResponseDto(StockedItem stockedItem) {
        return StockedItemResponseDto.builder()
                .id(stockedItem.getId())
                .ref(stockedItem.getRef().toString())
                .supportName(stockedItem.getSupport() != null ? stockedItem.getSupport().getName() : null)
                .structureName(stockedItem.getStructure() != null ? stockedItem.getStructure().getName() : null)
                .temperatureName(stockedItem.getTemperature() != null ? stockedItem.getTemperature().getName() : null)
                .isFragile(stockedItem.getIsFragile())
                .uvc(stockedItem.getUvc())
                .numberOfPackages(stockedItem.getNumberOfPackages())
                .dimension(stockedItem.getDimension() != null ? stockedItem.getDimension() : null)
                .price(stockedItem.getPrice())
                .storageOffer(stockedItem.getStorageOffer() != null ? stockedItem.getStorageOffer() : null)
                .storageNeed(stockedItem.getStorageNeed() != null ? stockedItem.getStorageNeed() : null)
                .build();
    }

    // Map request DTO to entity
    public static StockedItem fromRequestDto(StockedItemRequestDto dto, Support support, Structure structure,
                                             StackedLevel stackedLevel, Temperature temperature,
                                             Dimension dimension, StorageOffer storageOffer) {
        return StockedItem.builder()
                .support(support)
                .structure(structure)
                .temperature(temperature)
//                .isFragile(dto.getIsFragile())
                .uvc(dto.getUvc())
                .numberOfPackages(dto.getNumberOfPackages())
                .dimension(dimension)
                .price(dto.getPrice())
                .storageOffer(storageOffer)
                .build();
    }
}