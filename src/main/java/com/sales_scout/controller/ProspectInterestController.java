package com.sales_scout.controller;

import com.sales_scout.dto.request.ProspectInterestRequestDto;
import com.sales_scout.dto.response.ProspectInterestResponseDto;
import com.sales_scout.service.leads.ProspectInterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/prospects-interests")
public class ProspectInterestController {
    private final ProspectInterestService prospectInterestService;

    public ProspectInterestController(ProspectInterestService prospectInterestService) {
        this.prospectInterestService = prospectInterestService;
    }
    @PutMapping("/prospectId={prospectId}/interestId={interestId}")
    public ResponseEntity<ProspectInterestResponseDto> UpdateInterestById(@PathVariable Long prospectId, @PathVariable Long interestId , @RequestBody ProspectInterestRequestDto prospectinterestDetails){
        ProspectInterestResponseDto prospectInterest = prospectInterestService.updateProspectInterest(interestId,prospectId,prospectinterestDetails);
        return ResponseEntity.ok(prospectInterest);
    }

}
