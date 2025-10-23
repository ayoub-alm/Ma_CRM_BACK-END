package com.sales_scout.entity.crm.wms.invoice;

import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.data.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "storage_payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String ref;
    private String paymentMethod;
    private Double amount;
    private LocalDateTime receptionDate;
    private LocalDateTime validationDate;
    private boolean validationStatus;
}
