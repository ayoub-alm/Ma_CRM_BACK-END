package com.sales_scout.repository.crm.wms;

import com.sales_scout.entity.crm.wms.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {
    List<Structure> findAllByCompanyIdAndDeletedAtIsNull(Long companyId);
}
