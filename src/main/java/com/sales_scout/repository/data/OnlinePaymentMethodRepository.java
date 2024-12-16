package com.sales_scout.repository.data;



import com.sales_scout.entity.data.OnlinePaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePaymentMethodRepository extends JpaRepository<OnlinePaymentMethod, Long> {
}
