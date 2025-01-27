package com.sales_scout.service.leads;

import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.entity.leads.Prospect;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import com.sales_scout.specification.InteractionSpecification;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;
import com.sales_scout.repository.leads.ProspectRepository;
import com.sales_scout.repository.UserRepository;
import com.sales_scout.service.AuthenticationService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private final ProspectRepository prospectRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    public InteractionService(InteractionRepository interactionRepository,
                              ProspectRepository prospectRepository,
                              InterlocutorRepository interlocutorRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.interactionRepository = interactionRepository;
        this.prospectRepository = prospectRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Get all non-soft-deleted interactions.
     * @return List of InteractionResponseDto.
     */
    public List<InteractionResponseDto> getAllInteractions(InteractionType type, InteractionSubject subject) {
        Specification<Interaction> specification =  InteractionSpecification.hasInteractionTypeAndReport(type,subject);
        return interactionRepository.findAll(specification).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<InteractionResponseDto> getInteractionByInterlocutorId(Long InterlocutorId){
        return this.interactionRepository.findByInterlocutorId(InterlocutorId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a single interaction by ID.
     *
     * @param id Interaction ID.
     * @return InteractionResponseDto.
     */
    public InteractionResponseDto getInteractionById(Long id) {
        Interaction interaction = interactionRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new IllegalArgumentException("Interaction with ID " + id + " not found."));
        return convertToResponseDto(interaction);
    }
    /**
     * Create or update an interaction.
     *
     * @param interactionRequestDto InteractionRequestDto.
     * @return InteractionResponseDto.
     */
    public InteractionResponseDto saveOrUpdateInteraction(InteractionRequestDto interactionRequestDto) {
        // Validate required fields in InteractionRequestDto
        if (interactionRequestDto == null) {
            throw new IllegalArgumentException("InteractionRequestDto cannot be null.");
        }
        if (interactionRequestDto.getProspectId() == null) {
            throw new IllegalArgumentException("Prospect ID is required.");
        }

        // Fetch the associated Prospect
        Prospect prospect = prospectRepository.findById(interactionRequestDto.getProspectId())
                .orElseThrow(() -> new IllegalArgumentException("Prospect not found for ID: " + interactionRequestDto.getProspectId()));

        // Fetch the associated Interlocutor, if provided
        Interlocutor interlocutor = null;
        if (interactionRequestDto.getInterlocutorId() != null) {
            interlocutor = interlocutorRepository.findById(interactionRequestDto.getInterlocutorId())
                    .orElseThrow(() -> new IllegalArgumentException("Interlocutor not found for ID: " + interactionRequestDto.getInterlocutorId()));
        }
        UserEntity user = this.authenticationService.getCurrentUser();


        UserEntity affectedTo = null;
        if (interactionRequestDto.getAffectedToId() != null) {
            affectedTo = userRepository.findById(interactionRequestDto.getAffectedToId())
                    .orElseThrow(() -> new IllegalArgumentException("AffectedTo user not found for ID: " + interactionRequestDto.getAffectedToId()));
        }

        // Build the Interaction entity
        Interaction interaction = Interaction.builder()
                .prospect(prospect)
                .interlocutor(interlocutor)
                .agent(user)
                .affectedTo(affectedTo)
                .report(interactionRequestDto.getReport())
                .interactionSubject(interactionRequestDto.getInteractionSubject())
                .interactionType(interactionRequestDto.getInteractionType())
                .planningDate(interactionRequestDto.getPlanningDate())
                .joinFilePath(interactionRequestDto.getJoinFilePath())
                .address(interactionRequestDto.getAddress())
                .build();

        // Save the interaction entity
        interaction = interactionRepository.save(interaction);

        // Convert and return the response DTO
        return convertToResponseDto(interaction);
    }



    /**
     * Soft delete an interaction by ID.
     * @param id Interaction ID.
     * @return true if Interaction exsist else @return false
     */
    public boolean softDeleteInteraction(Long id) throws EntityNotFoundException {
            Optional<Interaction> interaction = interactionRepository.findByDeletedAtIsNullAndId(id);
            if (interaction.isPresent()) {
                interaction.get().setDeletedAt(LocalDateTime.now());
                interactionRepository.save(interaction.get());
                return true;
            }else {
                throw new EntityNotFoundException("Interaction with ID " + id + " not found or already deleted.");
            }
    }

    /**
     * Restore a soft-deleted interaction by ID.
     * @param id Interaction ID.
     * @return true if Interaction exsist else @return false
     */
    public boolean restoreInteraction(Long id) throws EntityNotFoundException {
        Optional<Interaction> interaction = interactionRepository.findByDeletedAtIsNotNullAndId(id);
        if (interaction.isPresent()) {
            interaction.get().setDeletedAt(null);
            interactionRepository.save(interaction.get());
            return true;
        }else {
            throw new EntityNotFoundException("Interaction with ID " + id + " not found or already restored.");
        }
    }

    /**
     * Convert Interaction entity to Response DTO.
     *
     * @param interaction Interaction entity.
     * @return InteractionResponseDto.
     */
    private InteractionResponseDto convertToResponseDto(Interaction interaction) {
        return InteractionResponseDto.builder()
                .id(interaction.getId())
                .prospectId(interaction.getProspect().getId())
                .prospectName(interaction.getProspect().getName())
                .interlocutorId(interaction.getInterlocutor() != null ? interaction.getInterlocutor().getId() : null)
                .interlocutorName(interaction.getInterlocutor() != null ? interaction.getInterlocutor().getFullName() : null)
                .report(interaction.getReport())
                .interactionSubject(interaction.getInteractionSubject())
                .interactionType(interaction.getInteractionType())
                .planningDate(interaction.getPlanningDate())
                .joinFilePath(interaction.getJoinFilePath())
                .address(interaction.getAddress())
                .affectedToId(interaction.getAffectedTo() != null  ?  interaction.getAffectedTo().getId() : null)
                .affectedToName(interaction.getAffectedTo() != null ? interaction.getAffectedTo().getName() : null)
                .createdAt(interaction.getCreatedAt())
                .agentName(interaction.getAgent().getName())
                .build();
    }


}