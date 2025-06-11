package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.request.create.wms.UnloadingTypeCreateDto;
import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.UnloadingType;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.UnloadingTypeMapper;
import com.sales_scout.repository.crm.wms.UnloadingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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

    /**
     * this function allows to create unloading type
     * @param unloadingTypeCreateDto data to create unloading type
     * @return {UnloadingTypeResponseDto} dto of created unloading type
     */
    public UnloadingTypeResponseDto createUnloadingType(UnloadingTypeCreateDto unloadingTypeCreateDto) {
        UnloadingType unloadingType = this.unloadingTypeRepository.save(UnloadingType.builder()
                .ref(UUID.randomUUID())
                .name(unloadingTypeCreateDto.getName())
                .unitOfMeasurement(unloadingTypeCreateDto.getUnitOfMeasurement())
                .company(Company.builder().id(unloadingTypeCreateDto.getCompanyId()).build())
                .initPrice(unloadingTypeCreateDto.getInitPrice())
                .itemOrder(unloadingTypeCreateDto.getOrder())
                .build());

        return UnloadingTypeMapper.toResponseDto(unloadingType);
    }

    /**
     * This function allows us to update unloading type
     * @param unloadingTypeCreateDto updated data
     * @return {UnloadingTypeResponseDto} updated unloading type
     */
    public UnloadingTypeResponseDto updateUnloadingType(UnloadingTypeCreateDto unloadingTypeCreateDto){
        UnloadingType unloadingType = this.unloadingTypeRepository.findById(unloadingTypeCreateDto.getId())
                .orElseThrow(() ->  new ResourceNotFoundException("unloading type not found with id"+ unloadingTypeCreateDto.getId(),"",""));
        unloadingType.setName(unloadingTypeCreateDto.getName());
        unloadingType.setItemOrder(unloadingTypeCreateDto.getOrder());
        unloadingType.setInitPrice(unloadingTypeCreateDto.getInitPrice());
        unloadingType.setUnitOfMeasurement(unloadingTypeCreateDto.getUnitOfMeasurement());
        return UnloadingTypeMapper.toResponseDto(unloadingTypeRepository.save(unloadingType));
    }
}
