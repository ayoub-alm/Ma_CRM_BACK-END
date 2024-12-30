package com.sales_scout.controller.leads;


import com.sales_scout.dto.response.leads_dashboard.CountsDto;
import com.sales_scout.dto.response.leads_dashboard.ProspectCountDto;
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

    @GetMapping("/prospect-per-status")
    public List<ProspectCountDto> getCountOfProspectsPerStatus(){
       return this.leadsDashboardService.getCountOfProspectsPerStatus();
    }


}
