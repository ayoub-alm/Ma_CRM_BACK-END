package com.sales_scout.controller;


import com.sales_scout.dto.response.leads_dashboard.ProspectCountDto;
import com.sales_scout.service.LeadsDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/leads/dashboard")
public class LeadsDashboardController {
    private final LeadsDashboardService leadsDashboardService;

    public LeadsDashboardController(LeadsDashboardService leadsDashboardService) {
        this.leadsDashboardService = leadsDashboardService;
    }

    @GetMapping("/prospect-per-status")
    public List<ProspectCountDto> getCountOfProspectsPerStatus(){
       return this.leadsDashboardService.getCountOfProspectsPerStatus();
    }
}
