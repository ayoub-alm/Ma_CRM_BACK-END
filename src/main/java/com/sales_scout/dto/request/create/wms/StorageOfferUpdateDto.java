package com.sales_scout.dto.request.create.wms;

import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.enums.crm.wms.LivreEnum;
import com.sales_scout.enums.crm.wms.NeedStatusEnum;
import com.sales_scout.enums.crm.wms.StorageReasonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class StorageOfferUpdateDto {
    private Long id;
    private UUID ref;
    private StorageReasonEnum storageReason;
    private NeedStatusEnum status;
    private LivreEnum liverStatus;
    private LocalDateTime expirationDate;
    private Long duration;
    private Long numberOfSku;
    private String productType;
    private Long customerId;
    private Long companyId;
    private List<StockedItemRequestDto> stockedItemsRequestDto;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementRequestDto> requirements;
    private Double price;
    private Long storageNeedId;
    private Long paymentTypeId;
    private int paymentDeadline;
    private Long interlocutorId;
    private String note;
    private Double managementFees;
    private Long numberOfReservedPlaces;
    private Double minimumBillingGuaranteed;


}
