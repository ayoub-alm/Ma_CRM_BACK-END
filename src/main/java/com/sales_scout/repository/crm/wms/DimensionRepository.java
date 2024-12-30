package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Long> {
}
