package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.entity.crm.wms.contract.StorageAnnexe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageAnnexeRepository extends JpaRepository<StorageAnnexe,Long> {
    int countByStorageContractId(Long id);

    List<StorageAnnexe> findByStorageContractId(Long id);
}
