package com.sales_scout.dto.request.create;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProjectDto {

    @NotBlank(message = "Le nom ne doit pas être vide")
    @Size(max = 100, message = "Le nom du projet ne peut pas dépasser 100 caractères.")
    @NotNull(message = "Le nom de la société est obligatoire")
    private String name;

    @NotNull(message = "L'ID de la société est obligatoire")
    private Long companyId;



    public CreateProjectDto(String name, Long companyId) {
        this.name = name;
        this.companyId = companyId;
    }


}
