package com.sales_scout.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UpdateProjectDto {

    @NotNull(message = "L'ID de projet est obligatoire")
    private Long projectId;

    @NotBlank(message = "Le nom ne doit pas être vide")
    @Size(max = 100, message = "Le nom du projet ne peut pas dépasser 100 caractères.")
    @NotNull(message = "Le nom de la société est obligatoire")
    private String name;


    @NotNull(message = "L'ID de la société est obligatoire")
    private Long companyId;
}
