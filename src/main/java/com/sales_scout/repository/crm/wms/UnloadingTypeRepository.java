package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.UnloadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnloadingTypeRepository extends JpaRepository<UnloadingType, Long> {

    List<UnloadingType> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
