package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.entity.crm.wms.contract.StorageContractUnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ContractUnloadingTypeRepository extends JpaRepository<StorageContractUnloadingType, Long> {
    List<StorageContractUnloadingType> findAllByStorageContractId(Long id);
}
