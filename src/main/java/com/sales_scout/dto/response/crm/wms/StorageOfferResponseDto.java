package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.BaseDto;
import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.crm.wms.offer.StorageOfferStatus;
import com.sales_scout.entity.data.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class StorageOfferResponseDto extends BaseDto {
    // Getters and Setters
    private Long id;
    private UUID ref;
    private String number;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private StorageOfferStatus status; // NeedStatusEnum as String
    private LocalDateTime expirationDate;
    private Long duration;
    private Long numberOfSku;
    private String productType;
    private CustomerDto customer;
    private double price;
    private StorageNeedResponseDto storageNeed;
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;
    private List<StorageOfferPaymentMethodDto> paymentTypes;
    private int paymentDeadline;
    private InterlocutorResponseDto interlocutor;
    private String note;
    private Double managementFees;
    private Long numberOfReservedPlaces;
    private Double minimumBillingGuaranteed;
    private Double minimumBillingGuaranteedFixed;
    private Long maxDisCountValue;
    private String devise;
}
