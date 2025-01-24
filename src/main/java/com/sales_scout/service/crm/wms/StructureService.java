package com.sales_scout.service.crm.wms;

import com.sales_scout.dto.response.crm.wms.SupportResponseDto;
import com.sales_scout.repository.crm.wms.StructureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StructureService {
    private final StructureRepository structureRepository;

    public StructureService(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    /**
     * this function allow to get structures By company id
     * @param companyId the id of the company
     * @return {List<SupportResponseDto>} list of structures
     */
    public List<SupportResponseDto> getAllStructureByCompanyId(Long companyId){
        return this.structureRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId).stream().map(data ->
                SupportResponseDto.builder()
                        .id(data.getId())
                        .name(data.getName())
                        .ref(String.valueOf(data.getRef()))
                        .build()
                ).toList();
    }
}
