package com.sales_scout.controller.leads;

import com.sales_scout.dto.EntityFilters.InteractionFilterRequestDto;
import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.service.leads.InteractionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    /**
     * Retrieves all interactions related to a specific company, optionally filtered by a search term.
     *
     * @param companyId the ID of the company for which to retrieve interactions; this is a required parameter.
     * @param searchValue an optional search term used to filter interactions based on matching fields such as report, address, subject, type, and other attributes.
     * @return a ResponseEntity containing a list of InteractionResponseDto objects that represent the retrieved interactions.
     */
    @GetMapping("")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractions(
            @RequestParam(required = true) Long companyId , @RequestParam(required = false) String searchValue
    ){
        List<InteractionResponseDto> interactions = this.interactionService.getAllInteractions(companyId, searchValue);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * Retrieves a specific interaction by its ID.
     *
     * @param id the unique identifier of the interaction to be retrieved
     * @return a ResponseEntity containing the InteractionResponse*/
    @GetMapping("/{id}")
    ResponseEntity<InteractionResponseDto> getInteractionById(@PathVariable Long id){
        InteractionResponseDto interactions = this.interactionService.getInteractionById(id);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * Retrieves all interactions associated with a specific interlocutor.
     *
     * @param interlocutorId the ID of the interlocutor for which to retrieve interactions
     * @return a ResponseEntity containing a list of InteractionResponseDto objects that represent
     *         the interactions associated with*/
    @GetMapping("/interlocutor/{interlocutorId}")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractionsByInterlocutorId(@PathVariable Long interlocutorId){
        List<InteractionResponseDto> interactions = this.interactionService.getInteractionByInterlocutorId(interlocutorId);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * Creates or updates an interaction based on the provided request data.
     *
     * @param interactionRequestDto the request DTO containing interaction details to be created or updated
     * @return a ResponseEntity containing the response DTO of the created or updated interaction
     * @throws IOException if an I/O error occurs during processing
     */
    @PostMapping("")
    ResponseEntity<InteractionResponseDto> createInteraction(@RequestBody InteractionRequestDto interactionRequestDto) throws IOException {
        InteractionResponseDto interaction = this.interactionService.saveOrUpdateInteraction(interactionRequestDto);
        return new ResponseEntity<>(interaction, HttpStatus.OK);
    }

    /**
     *
     * @param interactionId
     * @param file
     * @return
     */
    @PostMapping("/{interactionId}/upload-pdf")
    public ResponseEntity<String> uploadContractPdf(
            @PathVariable Long interactionId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Resolve absolute path (during dev only!)
            Path resourcePath = Paths.get("src/main/resources/interactions");
            if (!Files.exists(resourcePath)) {
                Files.createDirectories(resourcePath);
            }

            String fileName = "interactions-" + interactionId + "-" + System.currentTimeMillis() + ".pdf";
            Path filePath = resourcePath.resolve(fileName);

            Files.write(filePath, file.getBytes());

            // Save the URL that matches the static resource handler
            String fileUrl = "/files/interactions/" + fileName;
            interactionService.uploadInteractionFileReport(interactionId, fileUrl);

            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error while saving file: " + e.getMessage());
        }
    }

    /**
     * Soft deletes a specific interaction by marking it with a deletion timestamp. If the interaction
     * is already deleted or does not exist, a 404 NOT FOUND status is returned.
     *
     * @param id the ID of the interaction to be soft deleted
     * @return a ResponseEntity containing a boolean value indicating whether the interaction was
     *         successfully soft deleted (true) or not (false)
     * @throws EntityNotFoundException if the interaction is not found or already deleted
     */
    @DeleteMapping("/soft-delete/{id}")
    ResponseEntity<Boolean>  softDeleteInteraction(@PathVariable Long id)throws EntityNotFoundException{
       try{
           return ResponseEntity.ok().body(interactionService.softDeleteInteraction(id));
       }catch(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
       }
    }

    /**
     * Soft deletes multiple interactions by marking them with a deletion timestamp.
     * The operation updates the interactions identified by the provided IDs and persists the changes.
     *
     * @param ids a list of IDs representing the interactions to be soft deleted
     * @return a ResponseEntity containing a boolean indicating whether the operation was successful
     */
    @PostMapping("/soft-delete/bulk")
    ResponseEntity<Boolean> softDeleteInteractions(@RequestBody List<Long> ids){
        return ResponseEntity.ok(interactionService.softDeleteInteractions(ids));
    }

    /**
     * Restores a soft-deleted interaction by its ID. If the interaction is not found,
     * a response with HTTP status 404 is returned.
     *
     * @param id the ID of the interaction to restore
     * @return a ResponseEntity containing a boolean indicating success (true)
     *         or failure (false) of the restoration operation
     * @throws EntityNotFoundException if the interaction is not found
     */
    @PutMapping("/restore/{id}")
    ResponseEntity<Boolean> restoreInteraction(@PathVariable Long id) throws EntityNotFoundException{
        try {
            return ResponseEntity.ok().body(interactionService.restoreInteraction(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Exports interactions to an Excel file and returns the file as a byte array.
     * The exported interactions can be filtered by a specific list of interaction IDs or by company.
     *
     * @param companyId the ID of the company whose interactions are being exported
     * @param interactionIds optional list of interaction IDs to filter the exported interactions;
     *        if null or empty, all non-soft-deleted interactions for the company will be included
     * @return a ResponseEntity containing the Excel file as a byte array
     * @throws RuntimeException if an IOException occurs during the export process
     */
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportInteractionsToExcel(@RequestParam Long companyId,
                                                            @RequestBody(required = false) List<Long> interactionIds) {
        try {
            return ResponseEntity.ok(interactionService.exportInteractionsToExcel(interactionIds, companyId));
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'exportation du fichier Excel: " + e.getMessage());
        }
    }

    /**
     * Retrieves a list of interactions filtered by various criteria specified in the request body.
     *
     * @param interactionFilterRequestDto the request DTO containing filtering criteria, such as
     *        company ID, customer IDs, interlocutor IDs, agent IDs, affectedTo IDs, interaction subjects,
     *        interaction types, date range, and filter type ("AND" or "OR") for logical conditions
     * @return a ResponseEntity containing a list of InteractionResponseDto objects representing
     *         the filtered interactions
     */
    @PostMapping("/filter-by-fields")
    public ResponseEntity<List<InteractionResponseDto>> getAllInteractionsByFilterFields(
            @RequestBody InteractionFilterRequestDto interactionFilterRequestDto) {
        try {
            List<InteractionResponseDto> interactions = interactionService.getAllInteractionsBySearchFields(interactionFilterRequestDto);
            return new ResponseEntity<>(interactions, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching interactions: " + e.getMessage());
        }
    }



}
