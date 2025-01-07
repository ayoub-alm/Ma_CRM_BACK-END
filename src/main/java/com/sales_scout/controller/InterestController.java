package com.sales_scout.controller;



import com.sales_scout.dto.response.InterestResponseDto;

import com.sales_scout.service.InterestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;

    }

    /**
     function get Interest by ID prospect
      **/
    @GetMapping("/{prospectId}")
    public ResponseEntity<List<InterestResponseDto>> getAllInterestByCompanyId(@PathVariable Long prospectId){
        List<InterestResponseDto> interestResponseDtos = interestService.getAllInterestByProspectId(prospectId);
        return ResponseEntity.ok(interestResponseDtos);
    }



}
