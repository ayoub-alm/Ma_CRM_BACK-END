package com.sales_scout.repository.crm.wms.contract;

import com.sales_scout.dto.response.crm.wms.StockedItemResponseDto;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeRequirement;
import com.sales_scout.entity.crm.wms.contract.StorageAnnexeStockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractStockedItemRepository extends JpaRepository<StorageAnnexeStockedItem, Long> {
    List<StorageAnnexeStockedItem> findAllByAnnexeId(Long id);

    List<StorageAnnexeStockedItem> findByAnnexeId(Long id);

    @Query("SELECT s FROM StorageAnnexeStockedItem s WHERE s.annexe.storageContract.id = :contractId")
    List<StorageAnnexeStockedItem> findByAnnexeStorageContractId(@Param("contractId") Long contractId);
}
