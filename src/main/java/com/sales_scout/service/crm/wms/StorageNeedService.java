package com.sales_scout.service.crm.wms;


import com.sales_scout.dto.request.create.StorageNeedCreateDto;
import com.sales_scout.dto.response.crm.wms.StorageNeedResponseDto;
import com.sales_scout.entity.crm.wms.StorageNeed;
import com.sales_scout.mapper.StorageNeedMapper;
import com.sales_scout.repository.crm.wms.StorageNeedRepository;
import org.springframework.stereotype.Service;

@Service
public class StorageNeedService {
    private final StorageNeedRepository storageNeedRepository;
    private final StorageNeedMapper storageNeedMapper;
    public StorageNeedService(StorageNeedRepository storageNeedRepository, StorageNeedMapper storageNeedMapper) {
        this.storageNeedRepository = storageNeedRepository;
        this.storageNeedMapper = storageNeedMapper;
    }


    public StorageNeedResponseDto createStorageNeed(StorageNeedCreateDto storageNeedCreateDto){
        StorageNeed storageNeed = this.storageNeedRepository.save(
              this.storageNeedMapper.toEntity(storageNeedCreateDto)
        );
        return this.storageNeedMapper.toResponseDto(storageNeed);
    }
}
