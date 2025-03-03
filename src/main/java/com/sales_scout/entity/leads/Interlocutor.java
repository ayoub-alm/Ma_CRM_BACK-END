package com.sales_scout.entity.leads;


import com.sales_scout.entity.BaseEntity;
import com.sales_scout.entity.data.Department;
import com.sales_scout.entity.data.EmailAddress;
import com.sales_scout.entity.data.JobTitle;
import com.sales_scout.entity.data.PhoneNumber;
import com.sales_scout.enums.ActiveInactiveEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interlocutors")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Interlocutor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    @Nullable
    private Department department;
    @ManyToOne
    @JoinColumn(name = "phone_number_id", referencedColumnName = "id")
    private PhoneNumber phoneNumber;

    @ManyToOne
    @JoinColumn(name = "email_id", referencedColumnName = "id")
    private EmailAddress emailAddress;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ActiveInactiveEnum active = ActiveInactiveEnum.ACTIVE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_title_id", nullable = true)
    private JobTitle jobTitle;

}
