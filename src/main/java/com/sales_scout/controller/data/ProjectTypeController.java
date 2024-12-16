package com.sales_scout.controller.data;


import com.sales_scout.entity.data.ProjectType;
import com.sales_scout.service.data.ProjectTypeService;
import jakarta.persistence.EntityNotFoundException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project-types")
public class ProjectTypeController {
    private final ProjectTypeService projectTypeService;

    public ProjectTypeController(ProjectTypeService projectTypeService) {
        this.projectTypeService = projectTypeService;
    }

    /**
     * Retrieves all project types.
     *
     * @return a ResponseEntity containing a list of project types and a 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<ProjectType>> getAllProjectTypes() {
        List<ProjectType> projectTypes = projectTypeService.getAllProjectTypes();
        return ResponseEntity.ok(projectTypes);
    }

    /**
     * Retrieves a project type by its ID.
     *
     * @param id the ID of the project type to retrieve.
     * @return a ResponseEntity containing the project type if found, or a 404 Not Found status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectType> getProjectTypeById(@PathVariable Long id) {
        Optional<ProjectType> projectType = projectTypeService.getProjectTypeById(id);
        return projectType.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Adds a new project type.
     *
     * @param projectType the project type to add.
     * @return a ResponseEntity containing the added project type and a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<ProjectType> addProjectType(@RequestBody ProjectType projectType) {
        ProjectType createdProjectType = projectTypeService.addProjectType(projectType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectType);
    }

    /**
     * Updates an existing project type by its ID.
     *
     * @param id          the ID of the project type to update.
     * @param projectType the project type data to update.
     * @return a ResponseEntity containing the updated project type and a 200 OK status, or a 404 Not Found status if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectType> updateProjectType(@PathVariable Long id, @RequestBody ProjectType projectType) {
        try {
            ProjectType updatedProjectType = projectTypeService.updateProjectType(id, projectType);
            return ResponseEntity.ok(updatedProjectType);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Deletes a project type by its ID.
     *
     * @param id the ID of the project type to delete.
     * @return a ResponseEntity with a 204 No Content status if deleted successfully, or a 404 Not Found or 500 Internal Server Error status if an error occurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectType(@PathVariable Long id) {
        try {
            projectTypeService.deleteProjectType(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
