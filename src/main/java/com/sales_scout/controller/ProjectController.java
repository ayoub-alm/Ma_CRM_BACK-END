package com.sales_scout.controller;

import com.sales_scout.dto.request.create.CreateProjectDto;
import com.sales_scout.dto.request.update.UpdateProjectDto;
import com.sales_scout.entity.Project;
import com.sales_scout.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Validated
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Get all projects where deletedAt is null.
     *
     * @return ResponseEntity<List<Project>>
     */
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjectsWithDeletedAtIsNull();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    /**
     * Get a project by ID where deletedAt is null.
     *
     * @param id The ID of the project
     * @return ResponseEntity<Project>
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectByIdAndDeletedAtIsNull(id)
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("Le project n'existe pas"));
    }

    /**
     * Create a new project.
     *
     * @param projectDto The project to be created
     * @return ResponseEntity<Project>
     */
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody @Valid CreateProjectDto projectDto) {
        Project createdProject = projectService.createProject(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    /**
     * Update an existing project by ID.
     *
     * @param id The ID of the project to be updated
     * @param projectdto {Projectdto} The project details to be updated
     * @return ResponseEntity<Project>
     */
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,@Valid @RequestBody UpdateProjectDto projectdto) {
        try {
            Project updatedProject = projectService.updateProject(id, projectdto);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Permanently delete a project by ID.
     *
     * @param id The ID of the project to be deleted
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectService.deleteProject(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Soft-delete a project by ID.
     *
     * @param id The ID of the project to be soft-deleted
     * @return ResponseEntity<Void>
     */
    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteProject(@PathVariable Long id) {
        if (projectService.softDeleteProject(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Restore a soft-deleted project by ID.
     *
     * @param id The ID of the project to be restored
     * @return ResponseEntity<Void>
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreSoftDeleteProject(@PathVariable Long id) {
        if (projectService.restoreSoftDeleteProject(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get a list of projects by a given company ID where deletedAt is null.
     *
     * @param companyId The ID of the company
     * @return ResponseEntity<List<Project>>
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Project>> getProjectsByCompanyId(@PathVariable Long companyId) {
        List<Project> projects = projectService.getProjectsByCompanyId(companyId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
