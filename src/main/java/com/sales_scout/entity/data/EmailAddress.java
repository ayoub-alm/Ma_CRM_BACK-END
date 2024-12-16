package com.sales_scout.entity.data;

import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "email_addresses")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Invalid email address format")
    private String address;

    private String type = "Default";


    public void setAddress(String address) {
        if (!address.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address format");
        }
        this.address = address;
    }
}
