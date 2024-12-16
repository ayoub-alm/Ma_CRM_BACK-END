package com.sales_scout.service;

import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.respense.InteractionResponseDto;
import com.sales_scout.entity.Interaction;
import com.sales_scout.entity.Interlocutor;
import com.sales_scout.entity.Prospect;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.repository.InteractionRepository;
import com.sales_scout.repository.InterlocutorRepository;
import com.sales_scout.repository.ProspectRepository;
import com.sales_scout.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private final ProspectRepository prospectRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final UserRepository userRepository;
    public InteractionService(InteractionRepository interactionRepository,
                              ProspectRepository prospectRepository,
                              InterlocutorRepository interlocutorRepository, UserRepository userRepository) {
        this.interactionRepository = interactionRepository;
        this.prospectRepository = prospectRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all non-soft-deleted interactions.
     *
     * @return List of InteractionResponseDto.
     */
    public List<InteractionResponseDto> getAllInteractions() {
        return interactionRepository.findAllByDeletedAtIsNull().stream()
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
        UserEntity user = new UserEntity();
        user.setId(2);


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
     *
     * @param id Interaction ID.
     */
    public void softDeleteInteraction(Long id) {
        Interaction interaction = interactionRepository.findByDeletedAtIsNullAndId(id)
                .orElseThrow(() -> new IllegalArgumentException("Interaction not found."));
        interaction.setDeletedAt(java.time.LocalDateTime.now());
        interactionRepository.save(interaction);
    }

    /**
     * Restore a soft-deleted interaction by ID.
     *
     * @param id Interaction ID.
     */
    public void restoreInteraction(Long id) {
        Interaction interaction = interactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Interaction not found."));
        interaction.setDeletedAt(null);
        interactionRepository.save(interaction);
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