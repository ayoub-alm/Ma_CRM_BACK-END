package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.mapper.ProvisionMapper;
import com.sales_scout.repository.crm.wms.ProvisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvisionService {
    private final ProvisionRepository provisionRepository;

    public ProvisionService(ProvisionRepository provisionRepository) {
        this.provisionRepository = provisionRepository;
    }

    /**
     * This function allows to get all provision by company id
     * @param  companyId {Long} the ID of company
     * @return List<ProvisionResponseDto> list of provisions
     */
    public List<ProvisionResponseDto> getProvisionByCompanyId(Long companyId) {
        return this.provisionRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(ProvisionMapper::toDto)
                .collect(Collectors.toList());
    }
}
