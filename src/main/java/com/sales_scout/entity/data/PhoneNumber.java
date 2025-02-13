package com.sales_scout.entity.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sales_scout.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "phone_numbers")
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @JsonCreator
    public PhoneNumber(@JsonProperty("number") String number) {
        if (number == null || !number.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.number = number;
    }
}
