package com.sales_scout.controller.leads;


import com.sales_scout.dto.response.leads_dashboard.*;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.service.leads.LeadsDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leads/dashboard")
public class LeadsDashboardController {
    private final LeadsDashboardService leadsDashboardService;

    public LeadsDashboardController(LeadsDashboardService leadsDashboardService) {
        this.leadsDashboardService = leadsDashboardService;
    }

    @GetMapping("/counts")
    public ResponseEntity<CountsDto> getCountOfProspects(){
        return ResponseEntity.ok(this.leadsDashboardService.getCountOfProspects());
    }

    @GetMapping("/customer-per-status")
    public ResponseEntity<List<CustomerCountDto>> getCountOfProspectsPerStatus(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerStatus());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/customer-per-interest")
    public ResponseEntity<List<CustomerCountDto>> getCountOfProspectsPerInterest(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerByInterest());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/customer-per-seller")
    public ResponseEntity<List<CustomerCountDto>> getCountOfCustomerPerSeller(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerSeller());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/customer-per-city")
    public ResponseEntity<List<CustomerCountDto>> getCountOfCustomerPerCity(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerCity());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/customer-per-date")
    public ResponseEntity<List<CustomerCountDto>> getCountOfCustomerPerDate(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerDate());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/customer-per-industry")
    public ResponseEntity<List<CustomerCountDto>> getCountOfCustomerPerIndustry(){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerIndustry());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/interaction-per-seller")
    public ResponseEntity<List<InteractionCountDto>> getCountOfInteractionPerSeller()throws DataNotFoundException{
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfInteractionBySeller());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @GetMapping("/interaction-per-subject")
    public ResponseEntity<List<InteractionCountDto>> getCountOfInteractionPerSubject() throws DataNotFoundException {
        try {
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfInteractionBySubject());
        }catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }

    }

    @GetMapping("/interaction-per-type")
    public ResponseEntity<List<InteractionCountDto>> getCountOfInteractionPerType() throws DataNotFoundException{
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfInteractionByType());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }


    @GetMapping("/interlocutor-per-seller")
    public ResponseEntity<List<InterlocutorCountDto>> getCountOfInterlocutorBySeller()throws DataNotFoundException {
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfInterlocutorBySeller());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }



}
