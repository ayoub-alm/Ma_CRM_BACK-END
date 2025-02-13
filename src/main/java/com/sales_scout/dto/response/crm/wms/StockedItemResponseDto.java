package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.crm.wms.Dimension;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.entity.crm.wms.offer.StorageOffer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockedItemResponseDto {
    private Long id;
    private String ref;
    private String supportName;
    private String structureName;
    private Long stackedLevelName;
    private String temperatureName;
    private Boolean isFragile;
    private Integer uvc;
    private Integer numberOfPackages;
    private Dimension dimension;
    private Double price;
    private StorageOffer storageOffer;
    private StorageNeed storageNeed;
    private List<ProvisionResponseDto> provisionResponseDto;
}