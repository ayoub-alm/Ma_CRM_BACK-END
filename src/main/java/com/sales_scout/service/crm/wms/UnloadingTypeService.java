package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.mapper.UnloadingTypeMapper;
import com.sales_scout.repository.crm.wms.UnloadingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnloadingTypeService {
    private final UnloadingTypeRepository unloadingTypeRepository;

    public UnloadingTypeService(UnloadingTypeRepository unloadingTypeRepository) {
        this.unloadingTypeRepository = unloadingTypeRepository;
    }

    /**
     * This function allows to get unloading type by companyId
     * @param companyId the id of select company
     * @return List<UnloadingTypeResponseDto> list of unloading types
     */
    public List<UnloadingTypeResponseDto> getUnloadingTypesByCompanyId(Long companyId) {
        List<UnloadingType> unloadingTypes =  unloadingTypeRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId);
        return  unloadingTypes.stream().map(UnloadingTypeMapper::toResponseDto).collect(Collectors.toList());
    }
}
