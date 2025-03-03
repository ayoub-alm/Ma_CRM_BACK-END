package com.sales_scout.dto.request.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkCustomerEditRequestDto {
    private List<Long> customerIds;
    private Long statusId;
    private Long industryId;
    private Long cityId;
    private Long countryId;
    private Long companySizeId;
    private Long structureId;
    private Long legalStatusId;
    private Long createdById;
    private Long affectedToId;
}
