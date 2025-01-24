package com.sales_scout.entity.leads;

import com.sales_scout.entity.Customer;
import com.sales_scout.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_logs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TrackingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_type", nullable = false)
    private String actionType; // e.g., CREATE, UPDATE, DELETE, VIEW

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "details", length = 500)
    private String details;

    @ManyToOne
    @JoinColumn(name = "prospect_id", referencedColumnName = "id", nullable = true)
    private Prospect prospect;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "interaction_id", referencedColumnName = "id")
    private Interaction interaction;

    @ManyToOne
    @JoinColumn(name = "interlocutor_id", referencedColumnName = "id")
    private Interlocutor interlocutor;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // For soft delete
}
