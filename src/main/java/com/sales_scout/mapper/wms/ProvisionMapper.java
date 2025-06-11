package com.sales_scout.mapper.wms;

import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import org.springframework.stereotype.Component;

@Component
public class ProvisionMapper {

    public static ProvisionResponseDto toDto(StockedItemProvision entity) {
        if (entity == null || entity.getProvision() == null) {
            return null;
        }

        return ProvisionResponseDto.builder()
                .id(entity.getProvision().getId())
                .name(entity.getProvision().getName())
                .ref(entity.getProvision().getRef())
                .initPrice(entity.getInitPrice())
                .unitOfMeasurement(entity.getProvision().getUnitOfMeasurement())
                .notes(entity.getProvision().getNotes())
                .companyId(entity.getProvision().getCompany().getId()) // assumes Provision has a Company object
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .increaseValue(entity.getIncreaseValue())
                .salesPrice(entity.getSalesPrice())
                .stockedItemProvisionId(entity.getId())
                .order(entity.getProvision().getItemOrder())
//                .quantity(entity.getProvision().getQuantity()) // assuming Provision has a quantity field
                .build();
    }
}
