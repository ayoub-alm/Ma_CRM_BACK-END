package com.sales_scout.repository.crm.wms.credit_note;

import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageCreditNoteRepository extends JpaRepository<StorageCreditNote, Long> {
    List<StorageCreditNote> findByCompanyIdAndDeletedAtIsNull(Long companyId);
}
