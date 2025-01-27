package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
    Collection<Temperature> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
