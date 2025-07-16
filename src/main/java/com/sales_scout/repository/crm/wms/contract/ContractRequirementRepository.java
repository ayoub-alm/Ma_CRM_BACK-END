package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.dto.response.crm.wms.StorageRequirementResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRequirementRepository extends JpaRepository<StorageAnnexeRequirement, Long> {
    List<StorageAnnexeRequirement> findAllByAnnexeStorageContractId(Long storageContractId);

    List<StorageAnnexeRequirement> findByAnnexeId(Long id);
}
