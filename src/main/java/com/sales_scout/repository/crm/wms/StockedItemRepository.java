package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.StockedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockedItemRepository extends JpaRepository<StockedItem, Long> {
}
