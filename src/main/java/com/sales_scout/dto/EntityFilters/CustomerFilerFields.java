package com.sales_scout.dto.EntityFilters;
import com.sales_scout.enums.ProspectStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CustomerFilerFields {
    List<Long> statusIds;
    List<Long> industryIds;
    List<Long> cityIds;
    List<Long> countryIds;
    List<Long> companySizeIds;
    List<Long> structureIds;
    List<Long> legalStatusIds;
    List<Long> createdByIds;
    List<Long> updatedByIds;
    List<Long> affectedToIds;
    LocalDateTime createdAtStart;
    LocalDateTime createdAtEnd;
    LocalDateTime updatedAtStart;
    LocalDateTime updatedAtEnd;
    Long companyId;
    String filterType;
}
