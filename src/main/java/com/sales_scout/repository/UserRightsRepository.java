package com.sales_scout.repository;

import com.sales_scout.entity.UserRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRightsRepository extends JpaRepository<UserRights,Long> {
    /**
     * get User By id where right_Id = rightId And user_Id = userId
     * @param {userId}
     * @param {rightId}
     * @return {UserRights}
     */
    Optional<UserRights> findByUserIdAndRightIdAndDeletedAtIsNull(Long userId , Long rightId);
}
