package com.sales_scout.dto.request.create;


import com.sales_scout.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDepartmentRequestDTO {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Long companyId;
    @NotNull
    private Long responsibleId;
}
