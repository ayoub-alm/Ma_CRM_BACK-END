package com.sales_scout.service.crm.wms;

import com.sales_scout.dto.response.crm.wms.SupportResponseDto;
import com.sales_scout.repository.crm.wms.SupportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportService {
    private final SupportRepository supportRepository;

    public SupportService(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    /**
     * this function allow to get Supports By company id
     * @param companyId the id of the company
     * @return {List<SupportResponseDto>} list of supports
     */
    public List<SupportResponseDto> getAllSupportByCompanyId(Long companyId){
        return this.supportRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId).stream().map(data ->
                SupportResponseDto.builder()
                        .id(data.getId())
                        .name(data.getName())
                        .ref(String.valueOf(data.getRef()))
                        .build()
                ).toList();
    }
}
