package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.dto.response.InterlocutorResponseDto;
import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexe;
import com.sales_scout.entity.crm.wms.contract.StorageContract;
import com.sales_scout.entity.crm.wms.contract.StorageContractStatus;
import com.sales_scout.entity.data.PaymentMethod;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageContractResponseDto extends BaseEntity {
    // Getters and Setters
    private Long id;
    private UUID ref;
    private String number;
    private String liverStatus;  // LivreEnum as String
    private String storageReason; // StorageReasonEnum as String
    private StorageContractStatus status; // NeedStatusEnum as String
    private LocalDate expirationDate;
    private LocalDate initialDate;
    private LocalDate startDate;
    private Long duration;
    private int numberOfSku;
    private String productType;
    private CustomerDto customer;
    private double price;
    private StorageNeedResponseDto storageNeed;
    private List<StockedItemResponseDto> stockedItems;
    private List<UnloadingTypeResponseDto> unloadingTypes;
    private List<StorageRequirementResponseDto> requirements;
    private PaymentMethod paymentType;
    private int paymentDeadline;
    private InterlocutorResponseDto interlocutor;
    private String note;
    private Double managementFees;
    private Long numberOfReservedPlaces;
    private Double minimumBillingGuaranteed;
    private int noticePeriod;
    private LocalDate renewalDate;
    private Double declaredValueOfStock;
    private Double insuranceValue;
    private String pdfUrl;
    private boolean automaticRenewal;
    private StorageContractResponseDto parentContract;
    private List<StorageAnnexe> annexes;
    private StorageOfferResponseDto offer;
}
