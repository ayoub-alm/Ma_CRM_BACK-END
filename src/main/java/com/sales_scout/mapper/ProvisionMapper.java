package com.sales_scout.mapper;

import com.sales_scout.dto.request.create.wms.ProvisionRequestDto;
import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.enums.crm.DiscountTypeEnum;

public class ProvisionMapper {
    // Map entity to response DTO
    public static ProvisionResponseDto toDto(Provision provision) {
        if (provision == null) {
            return null;
        }

        ProvisionResponseDto dto = new ProvisionResponseDto();
        dto.setId(provision.getId());
        dto.setName(provision.getName());
        dto.setRef(provision.getRef());
        dto.setInitPrice(provision.getInitPrice());
        dto.setUnitOfMeasurement(provision.getUnitOfMeasurement());
        dto.setNotes(provision.getNotes());
        dto.setCompanyId(provision.getCompany() != null ? provision.getCompany().getId() : null);
        dto.setDiscountType(provision.getDiscountTypeEnum() );
        dto.setDiscountValue(provision.getDiscountValue());
        dto.setSalesPrice(provision.getSalesPrice());
        dto.setIsStoragePrice(provision.getIsStoragePrice());
        dto.setOrder(provision.getItemOrder());
        return dto;
    }

    // Map request DTO to entity
    public static Provision fromDto(ProvisionRequestDto dto, Company company) {
        if (dto == null || company == null) {
            return null;
        }

        return Provision.builder()
                .name(dto.getName())
                .initPrice(dto.getInitPrice())
                .notes(dto.getNotes())
                .unitOfMeasurement(dto.getUnitOfMeasurement())
                .company(company)
                .build();
    }
}
