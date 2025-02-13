package com.sales_scout.repository;


import com.sales_scout.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * this function allows to get non soft-deleted roles
     * @return {List<Role>}
     */
    List<Role> findByDeletedAtIsNull();

    /**
     * get role by id
     * @param  id {Long}
     * @return {Role}
     */
    Role findByIdAndDeletedAtIsNull(Long id);

    Role findByRoleAndDeletedAtIsNull(String role);
}
