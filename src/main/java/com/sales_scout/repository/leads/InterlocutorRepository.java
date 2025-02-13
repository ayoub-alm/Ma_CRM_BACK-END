package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Interlocutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterlocutorRepository extends JpaRepository<Interlocutor, Long>, JpaSpecificationExecutor<Interlocutor> {

    List<Interlocutor> findAllByDeletedAtIsNull();

    List<Interlocutor> findAllByCustomerId(Long prospectId);

    /**
     * Get non-Soft-deleted interlocutor by ID
     * @param id
     * @return
     */
    Optional<Interlocutor> findByDeletedAtIsNullAndId(Long id);

    /**
     * Get Soft-deleted interlocutor by ID
     * @param id
     * @return
     */
    Optional<Interlocutor> findByDeletedAtIsNotNullAndId(Long id);


    List<Interlocutor> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

    Collection<Interlocutor> findByDeletedAtIsNull();

    /**
     * this function allows to get count of Interlocutor per Seller
     * @return {List<Object[]>} return array of Interlocutor count by Seller
     */
    @Query("SELECT i.createdBy , COUNT(i.id) as count FROM Interlocutor i WHERE i.deletedAt IS NULL GROUP BY i.createdBy")
    List<Object[]> getCountOfInterlocutorPerSeller();


}
