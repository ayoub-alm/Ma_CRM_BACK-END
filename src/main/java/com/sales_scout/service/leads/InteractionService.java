package com.sales_scout.service.leads;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.EntityFilters.InteractionFilterRequestDto;
import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.mapper.UserMapper;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.specification.InteractionSpecification;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;

import com.sales_scout.repository.UserRepository;
import com.sales_scout.service.AuthenticationService;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jakarta.persistence.EntityNotFoundException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private static final String File_DIRECTORY = "src/main/resources/static/files/";
    private final CustomerRepository prospectRepository;
    private final InterlocutorRepository interlocutorRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    public InteractionService(InteractionRepository interactionRepository,
                              CustomerRepository prospectRepository,
                              InterlocutorRepository interlocutorRepository, UserRepository userRepository, AuthenticationService authenticationService, UserMapper userMapper) {
        this.interactionRepository = interactionRepository;
        this.prospectRepository = prospectRepository;
        this.interlocutorRepository = interlocutorRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves all interactions related to a specified company, optionally filtered by a search value.
     *
     * @param companyId the ID of the company to fetch interactions for.
     * @param searchValue an optional search term used to filter interactions by matching fields like report, address,
     *                    subject, type, and other related entity fields.
     * @return a list of InteractionResponseDto objects representing the interactions that match the specified criteria.
     */
    public List<InteractionResponseDto> getAllInteractions(Long companyId, String searchValue) {
        List<Interaction> interactions;
        if (searchValue != null && !searchValue.isEmpty()) {
            interactions = interactionRepository.searchAllFields(searchValue, companyId);
        } else {
            interactions = interactionRepository.findByCustomer_Company_IdAndDeletedAtIsNull(companyId);
        }
        return interactions.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public List<InteractionResponseDto> getInteractionByInterlocutorId(Long InterlocutorId){
        return this.interactionRepository.findByInterlocutorId(InterlocutorId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a single interaction by ID.
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
    public InteractionResponseDto saveOrUpdateInteraction(InteractionRequestDto interactionRequestDto) throws IOException {
        // Validate required fields in InteractionRequestDto
        Objects.requireNonNull(interactionRequestDto, "InteractionRequestDto cannot be null.");
        Objects.requireNonNull(interactionRequestDto.getCustomerId(), "Prospect ID is required.");

        // Fetch the associated Prospect (Customer)
        Customer prospect = prospectRepository.findById(interactionRequestDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Prospect not found for ID: " + interactionRequestDto.getCustomerId()));

        // Fetch the associated Interlocutor, if provided
        Interlocutor interlocutor = null;
        if (interactionRequestDto.getInterlocutorId() != null) {
            interlocutor = interlocutorRepository.findById(interactionRequestDto.getInterlocutorId())
                    .orElseThrow(() -> new EntityNotFoundException("Interlocutor not found for ID: " + interactionRequestDto.getInterlocutorId()));
        }

        // Fetch the current user (agent)
        UserEntity user = authenticationService.getCurrentUser();

        // Fetch the affected user if provided
        UserEntity affectedTo = null;
        if (interactionRequestDto.getAffectedToId() != null) {
            affectedTo = userRepository.findById(interactionRequestDto.getAffectedToId())
                    .orElseThrow(() -> new EntityNotFoundException("AffectedTo user not found for ID: " + interactionRequestDto.getAffectedToId()));
        }

        // Handle file path saving
        String filePath = null;
        if (interactionRequestDto.getJoinFilePath() != null && !interactionRequestDto.getJoinFilePath().isEmpty()) {
            filePath = saveFile(interactionRequestDto.getJoinFilePath());
        }

        // Build the Interaction entity
        Interaction interaction = Interaction.builder()
                .id(interactionRequestDto.getId())
                .customer(prospect)
                .interlocutor(interlocutor)
                .agent(user)
                .affectedTo(affectedTo)
                .report(interactionRequestDto.getReport())
                .interactionSubject(interactionRequestDto.getInteractionSubject())
                .interactionType(interactionRequestDto.getInteractionType())
                .planningDate(interactionRequestDto.getPlanningDate())
                .joinFilePath(filePath)
                .address(interactionRequestDto.getAddress())
                .interactionDate(interactionRequestDto.getInteractionDate())
                .build();

        if (interactionRequestDto.getAgentId() == null ){
            interaction.setCreatedBy(SecurityUtils.getCurrentUser());
        }else{
            interaction.setUpdatedBy(SecurityUtils.getCurrentUser());
            interaction.setUpdatedAt(LocalDateTime.now());
        }

        // Save the interaction entity
        interaction = interactionRepository.save(interaction);

        // Convert and return the response DTO
        return convertToResponseDto(interaction);
    }
    /**
     * check the file and size is good and return file path
     * @param base64File
     * @return file path
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public String saveFile(String base64File) throws IOException, IllegalArgumentException {

        byte[] fileBytes = Base64.getDecoder().decode(base64File);
        // check file size
        long maxFileSize = 10*1024*1024;
        if(fileBytes.length > maxFileSize){
            throw new IllegalArgumentException("La taille du fichier dépasse la limite de 10 Mo");
        }
        //check directory is existed
        File directory = new File(File_DIRECTORY);
        if(!directory.exists()){
            directory.mkdirs();
        }

        String fileName = "interaction_report"+ System.currentTimeMillis()+".pdf";
        Path filePath = Paths.get(File_DIRECTORY+fileName);

        return fileName;
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
     * Soft deletes multiple interactions by marking them with a deletion timestamp.
     * The method retrieves all interactions with the provided IDs and sets their
     * deletedAt timestamp to the current time. Each updated interaction is persisted
     * back to the database. If any of the IDs do not correspond to existing interactions,
     * an EntityNotFoundException is thrown.
     *
     * @param ids a list of IDs representing the interactions to be soft deleted.
     * @return true if the operation completes successfully.
     * @throws EntityNotFoundException if any of the specified interactions are not found.
     */
    @Transactional
    public boolean softDeleteInteractions(List<Long> ids) throws EntityNotFoundException {
        this.interactionRepository.findAllById(ids).forEach(interaction -> {
            interaction.setDeletedAt(LocalDateTime.now());
            interactionRepository.save(interaction);
        });
        return true;
    }

    /**
     * Restores a soft-deleted interaction by its ID, setting its deletion timestamp to null.
     * If the interaction is not found or is already restored, an EntityNotFoundException is thrown.
     *
     * @param id the ID of the interaction to restore.
     * @return true if the interaction was successfully restored.
     * @throws EntityNotFoundException if the interaction is not found or is already restored.
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
     * Exports interactions to an Excel file based on the provided interaction IDs or company ID.
     * If interaction IDs are provided, it fetches the corresponding interactions by their IDs.
     * If interaction IDs are not provided, it retrieves interactions for the specified company ID.
     * The exported Excel file contains a row for each interaction and columns for relevant attributes.
     *
     * @param interactionIds a list of IDs representing the interactions to export.
     *                        If null or empty, interactions will be fetched using the company ID.
     * @param companyId       the ID of the company to fetch interactions for if interaction IDs are not provided.
     * @return a byte array representing the generated Excel file.
     * @throws IOException if an I/O error occurs during the file generation process.
     */
    public byte[] exportInteractionsToExcel(List<Long> interactionIds, Long companyId) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            List<Interaction> interactions;
            if (interactionIds != null && !interactionIds.isEmpty() && companyId != null && companyId > 0L) {
                interactions = interactionRepository.findAllById(interactionIds);
            } else {
                interactions = interactionRepository.findByCustomer_Company_IdAndDeletedAtIsNull(companyId);
            }
            Sheet sheet = workbook.createSheet("Interactions");
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Sujet", "Type d'Interaction", "Date", "Client", "Adresse", "commercial","Affecté à",
                    "Date de planification" ,"Date de création"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Interaction interaction : interactions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(interaction.getId());
                row.createCell(1).setCellValue(interaction.getInteractionSubject().name());
                row.createCell(2).setCellValue(interaction.getInteractionType().name());
                row.createCell(3).setCellValue(interaction.getCreatedAt().toString());
                row.createCell(4).setCellValue(interaction.getCustomer().getName());
                row.createCell(5).setCellValue(interaction.getAddress());
                row.createCell(6).setCellValue(interaction.getAgent().getName());
                row.createCell(7).setCellValue(interaction.getAffectedTo() != null ? interaction.getAffectedTo().getName() : null);
                row.createCell(8).setCellValue(interaction.getPlanningDate() != null ? interaction.getPlanningDate().toString() : null);
                row.createCell(9).setCellValue(interaction.getCreatedAt().toString());
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
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
                .customerId(interaction.getCustomer().getId())
                .prospectName(interaction.getCustomer().getName())
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
                .interactionDate(interaction.getInteractionDate())
                .createdBy(interaction.getCreatedBy() != null ? userMapper.fromEntity(interaction.getCreatedBy()) : null)
                .updatedBy(interaction.getUpdatedBy() != null ? userMapper.fromEntity(interaction.getUpdatedBy()) : null)
                .build();
    }

    /**
     * Retrieves a list of interactions based on the specified filtering criteria.
     * The filtering is determined by the properties provided in the interactionFilterFields object.
     * Supports both AND and OR logical conditions for filtering.
     *
     * @param interactionFilterFields an object containing various filtering criteria, such as customer IDs,
     *                                interlocutor IDs, agent IDs, affectedTo IDs, interaction subjects*/
    public List<InteractionResponseDto> getAllInteractionsBySearchFields(InteractionFilterRequestDto interactionFilterFields) {
        boolean useOrCondition = interactionFilterFields.getFilterType().equals("OR");
        Specification<Interaction> specification = InteractionSpecification.hasInteractionFilter(interactionFilterFields, useOrCondition);
        return interactionRepository.findAll(specification).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
}