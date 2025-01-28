package com.sales_scout.service.leads;

import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.entity.leads.Customer;
import com.sales_scout.entity.leads.Interaction;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.entity.UserEntity;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import com.sales_scout.repository.leads.CustomerRepository;
import com.sales_scout.specification.InteractionSpecification;
import com.sales_scout.repository.leads.InteractionRepository;
import com.sales_scout.repository.leads.InterlocutorRepository;

import com.sales_scout.repository.UserRepository;
import com.sales_scout.service.AuthenticationService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public InteractionService(InteractionRepository interactionRepository,
                              CustomerRepository prospectRepository,
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
    public InteractionResponseDto saveOrUpdateInteraction(InteractionRequestDto interactionRequestDto) throws IOException {
        // Validate required fields in InteractionRequestDto
        if (interactionRequestDto == null) {
            throw new IllegalArgumentException("InteractionRequestDto cannot be null.");
        }
        if (interactionRequestDto.getCustomerId() == null) {
            throw new IllegalArgumentException("Prospect ID is required.");
        }

        // Fetch the associated Prospect
        Customer prospect = prospectRepository.findById(interactionRequestDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Prospect not found for ID: " + interactionRequestDto.getCustomerId()));

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

        // save path file form interaction request dto
        String filePath = null;
        if (interactionRequestDto.getJoinFilePath()!= null ){
            filePath = saveFile(interactionRequestDto.getJoinFilePath());
        }

        // Build the Interaction entity
        Interaction interaction = Interaction.builder()
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
                .build();

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

    public void exportFileExcel(List<Interaction> interactions , String filePath)throws IOException {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Interaction");
            Row headerRow = sheet.createRow(0);
            String[] colmuns={"Id","Address","Interaction Subject"
                    , "Interaction Type","Planning Date","Report","Affected To"
                    ,"Agent" , "Interlocutor","Prospect"};

            for (int i = 0; i < colmuns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colmuns[i]);
            }

            int rowNum = 1;
            if(interactions == null){
              interactions = interactionRepository.findAll();
            }

            for(Interaction interaction : interactions){
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(interaction.getId());
                    row.createCell(1).setCellValue(interaction.getAddress());
                    row.createCell(2).setCellValue(interaction.getInteractionSubject().name());
                    row.createCell(3).setCellValue(interaction.getInteractionType().name());
                    row.createCell(4).setCellValue(interaction.getPlanningDate());
                    row.createCell(5).setCellValue(interaction.getReport());
                    row.createCell(6).setCellValue(interaction.getAffectedTo().getName());
                    row.createCell(7).setCellValue(interaction.getAgent().getName());
                    row.createCell(8).setCellValue(interaction.getInterlocutor().getFullName());
                    row.createCell(9).setCellValue(interaction.getCustomer().getName());
            }

            for(int i = 0 ; i < colmuns.length;i++){
                sheet.autoSizeColumn(i);
            }
            try(FileOutputStream fileOutput= new FileOutputStream(filePath)){
                workbook.write(fileOutput);
            }
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
                .build();
    }


}