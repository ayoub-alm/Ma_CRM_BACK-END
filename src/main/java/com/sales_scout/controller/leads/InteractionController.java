package com.sales_scout.controller.leads;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;

import com.sales_scout.entity.leads.Interaction;

import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;

import com.sales_scout.service.leads.InteractionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    /**
     * get all interactions
     * @return
     */
    @GetMapping("")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractions(
            @RequestParam(required = false) InteractionType type,
            @RequestParam(required = false)InteractionSubject subject
    ){
        List<InteractionResponseDto> interactions = this.interactionService.getAllInteractions(type,subject);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * get Interactions By Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    ResponseEntity<InteractionResponseDto> getInteractionById(@PathVariable Long id){
        InteractionResponseDto interactions = this.interactionService.getInteractionById(id);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * get all Interaction By interlocutor ID
     * @param interlocutorId the ID of interlocutor
     * @return ResponseEntity<List<InteractionResponseDto>>
     */
    @GetMapping("/interlocutor/{interlocutorId}")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractionsByInterlocutorId(@PathVariable Long interlocutorId){
        List<InteractionResponseDto> interactions = this.interactionService.getInteractionByInterlocutorId(interlocutorId);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    /**
     * create interaction
     * @param interactionRequestDto the interaction to create
     * @return interactionRequestDto
     * @throws IOException
     */
    @PostMapping("")
    ResponseEntity<InteractionResponseDto> createInteraction(@RequestBody InteractionRequestDto interactionRequestDto) throws IOException {
        InteractionResponseDto interaction = this.interactionService.saveOrUpdateInteraction(interactionRequestDto);
        return new ResponseEntity<>(interaction, HttpStatus.OK);
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportProspectsToExcel(@RequestParam(required = false) String interactionsJson){
        try{
            List<Interaction> interactions = null;
            if (interactionsJson != null && !interactionsJson.isEmpty()){
                ObjectMapper objectMapper = new ObjectMapper();
                interactions = objectMapper.readValue(interactionsJson, new TypeReference<List<Interaction>>() {
                });
            }
            interactionService.exportFileExcel(interactions , "Interaction_File.xlsx");
            return ResponseEntity.ok("Excel File exported successfuly: Interactions_file.xlsx");
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to export Excel file " + e.getMessage());
        }
    }
    /**
     * Soft Delete Interaction By Id
     * @param id
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
     * Restore Interaction By Id
     * @param id
     */
    @PutMapping("/restore/{id}")
    ResponseEntity<Boolean> restoreInteraction(@PathVariable Long id) throws EntityNotFoundException{
        try {
            return ResponseEntity.ok().body(interactionService.restoreInteraction(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
