package com.sales_scout.controller.leads;


import com.sales_scout.dto.request.create.InteractionRequestDto;
import com.sales_scout.dto.response.InteractionResponseDto;
import com.sales_scout.entity.EntityFilters.InteractionFilter;
import com.sales_scout.entity.leads.Interlocutor;
import com.sales_scout.enums.InteractionSubject;
import com.sales_scout.enums.InteractionType;
import com.sales_scout.service.UserAgentService;
import com.sales_scout.service.leads.InteractionService;
import com.sales_scout.service.leads.InterlocutorService;
import com.sales_scout.service.leads.ProspectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/interaction")
public class InteractionController {
    private final InteractionService interactionService;
    private final ProspectService prospectService;
    private final UserAgentService userAgentService;
    private final InterlocutorService interlocutorService;

    public InteractionController(InteractionService interactionService, ProspectService prospectService, UserAgentService userAgentService, InterlocutorService interlocutorService) {
        this.interactionService = interactionService;
        this.prospectService = prospectService;
        this.userAgentService = userAgentService;
        this.interlocutorService = interlocutorService;
    }

    /**
     * Get all non-soft-deleted interactions with specification
     */
    @GetMapping("")
    ResponseEntity<List<InteractionResponseDto>> getAllInteractions(
            @RequestParam(required = false) InteractionSubject interactionSubject,
            @RequestParam(required = false) InteractionType interactionType,
            @RequestParam(required = false) Date createdAtFrom,
            @RequestParam(required = false) Date createdAtTo,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long interlocutorId,
            @RequestParam(required = false) Date planningDate,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Long agentId,
            @RequestParam(required = false) Long affectedToId,
            @RequestParam(required = false) String report
    ){
        // creation object interactionFilter
        InteractionFilter interactionFilter = new InteractionFilter();
        interactionFilter.setInteractionSubject(interactionSubject);
        interactionFilter.setInteractionType(interactionType);
        interactionFilter.setCreatedAtFrom(createdAtFrom);
        interactionFilter.setCreatedAtTo(createdAtTo);
        interactionFilter.setId(id);
        // Handle interlocutorId
        if (interlocutorId != null) {
            Interlocutor interlocutor = new Interlocutor();
            interlocutor.setId(interlocutorId);
            interactionFilter.setInterlocutor(interlocutor);
        }
        interactionFilter.setPlanningDate(planningDate);
        interactionFilter.setAddress(address);

        // Handle agentId
        if (agentId != null) {
            interactionFilter.setAgent(userAgentService.findById(agentId));
        }

        // Handle affectedToId
        if (affectedToId != null) {
            interactionFilter.setAffectedTo(userAgentService.findById(affectedToId));
        }interactionFilter.setReport(report);
        List<InteractionResponseDto> interactions = this.interactionService.getAllInteractions(interactionFilter);
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
}
