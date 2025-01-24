package com.sales_scout.service.crm.wms;

import com.sales_scout.dto.response.crm.wms.SupportResponseDto;
import com.sales_scout.dto.response.crm.wms.TemperatureResponseDto;
import com.sales_scout.repository.crm.wms.SupportRepository;
import com.sales_scout.repository.crm.wms.TemperatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemperatureService {
    private final TemperatureRepository temperatureRepository;

    public TemperatureService(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }


    /**
     * this function allow to get temperatures By company id
     * @param companyId the id of the company
     * @return {List<TemperatureResponseDto>} list of temperatures
     */
    public List<TemperatureResponseDto> getAllTemperatureByCompanyId(Long companyId){
        return this.temperatureRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId).stream().map(data ->
                        TemperatureResponseDto.builder()
                        .id(data.getId())
                        .name(data.getName())
                        .ref(String.valueOf(data.getRef()))
                        .build()
                ).toList();
    }
}
