package com.sales_scout.service;

import com.sales_scout.dto.request.create.CreateProjectDto;
import com.sales_scout.dto.request.update.UpdateProjectDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.Project;
import com.sales_scout.repository.CompanyRepository;
import com.sales_scout.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    public ProjectService(ProjectRepository projectRepository, CompanyRepository companyRepository) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * Get all projects where deletedAt is null (not soft-deleted).
     *
     * @return List<Project>
     */
    public List<Project> getAllProjectsWithDeletedAtIsNull() {
        return this.projectRepository.findAllByDeletedAtIsNull();
    }

    /**
     * Get a project by ID where deletedAt is null.
     *
     * @param id The id of the project
     * @return Optional<Project>
     */
    public Optional<Project> getProjectByIdAndDeletedAtIsNull(Long id) {
        return this.projectRepository.findByDeletedAtIsNullAndId(id);
    }

    /**
     * Create a new project.
     *
     * @param projectDto The project to be created
     * @return Project The created project
     */
    public Project createProject(CreateProjectDto projectDto) throws RuntimeException {
        Project newProject = new Project();
        Company company = this.companyRepository.findById(projectDto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        newProject.setName(projectDto.getName());
        newProject.setCompany(company);
        return projectRepository.save(newProject);
    }

    /**
     * Update an existing project by its ID.
     *
     * @param id The ID of the project to be updated
     * @param projectDto The project details to be updated
     * @return Project The updated project
     * @throws EntityNotFoundException If the project doesn't exist
     */
    public Project updateProject(Long id, UpdateProjectDto projectDto) throws EntityNotFoundException {
        Optional<Project> projectOptional = this.getProjectByIdAndDeletedAtIsNull(id);
        if (projectOptional.isPresent()) {
            Project existingProject = projectOptional.get();
            // Update Name
            existingProject.setName(projectDto.getName());
            Optional<Company> companyOptional = this.companyRepository.findByDeletedAtIsNullAndId(projectDto.getCompanyId());
            //
            companyOptional.ifPresent(existingProject::setCompany);
            // Add other fields as necessary
            return this.projectRepository.save(existingProject);
        } else {
            throw new EntityNotFoundException("Le project n'existe pas");
        }
    }

    /**
     * Permanently delete a project by its ID.
     *
     * @param id The ID of the project to be deleted
     * @return Boolean Status of the delete operation
     */
    public Boolean deleteProject(Long id) {
        Optional<Project> projectOptional = this.getProjectByIdAndDeletedAtIsNull(id);
        if (projectOptional.isPresent()) {
            this.projectRepository.delete(projectOptional.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Soft-delete a project by its ID (set deletedAt timestamp).
     *
     * @param id The ID of the project to be soft-deleted
     * @return Boolean Status of the soft-delete operation
     */
    public Boolean softDeleteProject(Long id) {
        Optional<Project> projectOptional = this.getProjectByIdAndDeletedAtIsNull(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setDeletedAt(LocalDateTime.now());
            this.projectRepository.save(project);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Restore a soft-deleted project by its ID (set deletedAt to null).
     *
     * @param id The ID of the project to be restored
     * @return Boolean Status of the restore operation
     */
    public Boolean restoreSoftDeleteProject(Long id) {
        Optional<Project> projectOptional = this.projectRepository.findByIdAndDeletedAtIsNotNull(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setDeletedAt(null);
            this.projectRepository.save(project);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a list of projects by a given company ID where deletedAt is not null.
     *
     * @param companyId The ID of the company
     * @return List<Project> List of projects of the given company ID
     */
    public List<Project> getProjectsByCompanyId(Long companyId) {
        return this.projectRepository.findByDeletedAtIsNullAndCompanyId(companyId);
    }
}
