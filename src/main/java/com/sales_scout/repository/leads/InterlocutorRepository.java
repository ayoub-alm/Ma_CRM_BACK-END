package com.sales_scout.repository.leads;

import com.sales_scout.entity.leads.Interlocutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterlocutorRepository extends JpaRepository<Interlocutor, Long> {

    List<Interlocutor> findAllByDeletedAtIsNull();

    List<Interlocutor> findAllByProspectId(Long prospectId);

    Optional<Interlocutor> findByDeletedAtIsNullAndId(Long id);


    List<Interlocutor> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

    Collection<Interlocutor> findByDeletedAtIsNull();
}