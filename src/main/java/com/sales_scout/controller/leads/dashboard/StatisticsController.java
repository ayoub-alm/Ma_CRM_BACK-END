package com.sales_scout.controller.leads.dashboard;

import com.sales_scout.dto.EntityFilters.CustomerFilerFields;
import com.sales_scout.dto.EntityFilters.InteractionFilterRequestDto;
import com.sales_scout.dto.EntityFilters.InterlocutorsFilterRequestDto;
import com.sales_scout.dto.response.leads_dashboard.DashboardCountDto;
import com.sales_scout.dto.response.leads_dashboard.InteractionCountDto;
import com.sales_scout.dto.response.leads_dashboard.InterlocutorCountDto;
import com.sales_scout.service.leads.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /** CUSTOMER STATISTICS **/

    @PostMapping("/customers/count-by-status")
    public ResponseEntity<List<DashboardCountDto>> getCustomerCountByStatus(@RequestBody CustomerFilerFields filters) {
        return ResponseEntity.ok().body(statisticsService.getCustomerCountByStatus(filters));
    }

    @PostMapping("/customers/count-by-industry")
    public ResponseEntity<List<DashboardCountDto>> getCustomerCountByIndustry(@RequestBody CustomerFilerFields filters) {
        return ResponseEntity.ok().body(statisticsService.getCustomerCountByIndustry(filters));
    }

    @PostMapping("/customers/count-by-city")
    public ResponseEntity<List<DashboardCountDto>>  getCustomerCountByCity(@RequestBody CustomerFilerFields filters) {
        return ResponseEntity.ok().body(statisticsService.getCustomerCountByCity(filters, true));
    }

//    @PostMapping("/customers/count-by-country")
//    public ResponseEntity<List<DashboardCountDto>>  getCustomerCountByCountry(@RequestBody CustomerFilerFields filters) {
//        return ResponseEntity.ok().body(statisticsService.getCustomerCountByCountry(filters, true));
//    }
//
//    @PostMapping("/customers/count-by-company-size")
//    public ResponseEntity<List<DashboardCountDto>>  getCustomerCountByCompanySize(@RequestBody CustomerFilerFields filters) {
//        return ResponseEntity.ok().body(statisticsService.getCustomerCountByCompanySize(filters, true));
//    }
//
//    @PostMapping("/customers/count-by-legal-status")
//    public ResponseEntity<List<DashboardCountDto>>  getCustomerCountByLegalStatus(@RequestBody CustomerFilerFields filters) {
//        return ResponseEntity.ok().body(statisticsService.getCustomerCountByLegalStatus(filters, true));
//    }
//
//    @PostMapping("/customers/count-by-date")
//    public ResponseEntity<DashboardCountDto> getCustomerCountByDate(@RequestBody CustomerFilerFields filters) {
//        return ResponseEntity.ok().body(statisticsService.getCustomerCountByDate(filters, true));
//    }

    @PostMapping("/customers/count-by-seller")
    public ResponseEntity<List<DashboardCountDto>> getCustomerCountBySeller(@RequestBody CustomerFilerFields filters) {
        return ResponseEntity.ok().body(statisticsService.getCustomerCountBySeller(filters));
    }

    /** INTERACTION STATISTICS **/

//    @PostMapping("/interactions/count-by-type")
//    public ResponseEntity<DashboardCountDto> getInteractionCountByType(@RequestBody InteractionFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInteractionCountByType(filters, true));
//    }
//
//    @PostMapping("/interactions/count-by-subject")
//    public ResponseEntity<DashboardCountDto> getInteractionCountBySubject(@RequestBody InteractionFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInteractionCountBySubject(filters, true));
//    }
//
//    @PostMapping("/interactions/count-by-seller")
//    public ResponseEntity<DashboardCountDto> getInteractionCountBySeller(@RequestBody InteractionFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInteractionCountBySeller(filters, true));
//    }
//
//    /** INTERLOCUTOR STATISTICS **/
//
//    @PostMapping("/interlocutors/count-by-status")
//    public ResponseEntity<DashboardCountDto> getInterlocutorCountByStatus(@RequestBody InterlocutorsFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInterlocutorCountByStatus(filters, true));
//    }

//    @PostMapping("/interlocutors/count-by-job-title")
//    public ResponseEntity<DashboardCountDto> getInterlocutorCountByJobTitle(@RequestBody InterlocutorsFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInterlocutorCountByJobTitle(filters, true));
//    }
//
//    @PostMapping("/interlocutors/count-by-department")
//    public ResponseEntity<DashboardCountDto> getInterlocutorCountByDepartment(@RequestBody InterlocutorsFilterRequestDto filters) {
//        return ResponseEntity.ok().body(statisticsService.getInterlocutorCountByDepartment(filters, true));
//    }
}
