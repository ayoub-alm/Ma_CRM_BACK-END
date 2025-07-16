package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeUnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractUnloadingTypeRepository extends JpaRepository<StorageAnnexeUnloadingType, Long> {
    List<StorageAnnexeUnloadingType> findAllByAnnexeStorageContractId(Long id);

    List<StorageAnnexeUnloadingType> findByAnnexeId(Long id);

    Iterable<? extends StorageAnnexeUnloadingType> findAllByAnnexeId(Long id);
}
