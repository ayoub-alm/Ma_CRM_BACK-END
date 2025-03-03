package com.sales_scout.controller.leads;


import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.response.leads_dashboard.*;
import com.sales_scout.exception.DataNotFoundException;
import com.sales_scout.service.leads.LeadsDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/customer-per-status")
    public ResponseEntity<List<DashboardCountDto>> getCountOfProspectsPerStatus(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerStatus(customerFilerFields, false));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }


    @PostMapping("/customer-per-seller")
    public ResponseEntity<List<DashboardCountDto>> getCountOfCustomerPerSeller(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerSeller(customerFilerFields, false));
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



    @PostMapping("/customer-per-city")
    public ResponseEntity<List<DashboardCountDto>> getCountOfCustomerPerCity(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerCity(customerFilerFields, false));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @PostMapping("/customer-per-date")
    public ResponseEntity<List<DashboardCountDto>> getCountOfCustomerPerDate(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerDate(customerFilerFields, false));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @PostMapping("/customer-per-industry")
    public ResponseEntity<List<DashboardCountDto>> getCountOfCustomerPerIndustry(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerIndustry(customerFilerFields,false));
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage(),e.getCode());
        }
    }

    @PostMapping("/customer-per-structure")
    public ResponseEntity<List<DashboardCountDto>> getCountOfCustomerPerStructure(@RequestBody CustomerFilerFields customerFilerFields){
        try{
            return ResponseEntity.ok().body(this.leadsDashboardService.getCountOfCustomerPerStructure(customerFilerFields,false));
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
