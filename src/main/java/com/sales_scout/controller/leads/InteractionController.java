package com.sales_scout.controller.leads;


import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.service.leads.InteractionService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @GetMapping("")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractions(){
        List<InteractionResponseDto> interactions = this.interactionService.getAllInteractions();
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<InteractionResponseDto> getInteractionById(@PathVariable Long id){
        InteractionResponseDto interactions = this.interactionService.getInteractionById(id);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }



    @GetMapping("/interlocutor/{interlocutorId}")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractionsByInterlocutorId(@PathVariable Long interlocutorId){
        List<InteractionResponseDto> interactions = this.interactionService.getInteractionByInterlocutorId(interlocutorId);
        return new ResponseEntity<>(interactions, HttpStatus.OK);
    }


    @PostMapping("/create")
    ResponseEntity<InteractionResponseDto> createInteraction(@RequestBody InteractionRequestDto interactionRequestDto){
        InteractionResponseDto interaction = this.interactionService.saveOrUpdateInteraction(interactionRequestDto);
        return new ResponseEntity<>(interaction, HttpStatus.OK);
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
