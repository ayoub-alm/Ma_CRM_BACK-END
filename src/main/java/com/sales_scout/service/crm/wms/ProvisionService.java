package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.request.create.wms.ProvisionCreateDto;
import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.mapper.ProvisionMapper;
import com.sales_scout.repository.crm.wms.ProvisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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

    /**
     * this function allows to create a new storage provision
     * @param provisionCreateDto data of provision to create
     * @return {ProvisionResponseDto} created provision
     */
    public ProvisionResponseDto createProvision(ProvisionCreateDto provisionCreateDto) {
        Provision provision =  this.provisionRepository.save(Provision.builder()
                .ref(UUID.randomUUID())
                .name(provisionCreateDto.getName())
                .unitOfMeasurement(provisionCreateDto.getUnitOfMeasurement())
                .company(Company.builder().id(provisionCreateDto.getCompanyId()).build())
                .initPrice(provisionCreateDto.getInitPrice())
                .build());

        return ProvisionMapper.toDto(provision);
    }
}
