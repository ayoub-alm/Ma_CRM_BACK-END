package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.StockedItemProvision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface StockedItemProvisionRepository extends JpaRepository<StockedItemProvision, Long> {
    void deleteByStockedItemId(Long stockedItemId);

    @Modifying
    @Query("DELETE FROM StockedItemProvision p WHERE p.id = :id")
    void hardDeleteById(@Param("id") Long id);

    List<StockedItemProvision> findByStockedItemId(Long id);
}
