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
     function get Interest by Company Id
     **/
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<InterestResponseDto>> getAllInterestByCompanyId(@PathVariable Long companyId){
        List<InterestResponseDto> interestResponseDtos = interestService.getAllInterestByCompanyId(companyId);
        return ResponseEntity.ok(interestResponseDtos);
    }

    /**
     function get Interest by prospect ID
      **/
    @GetMapping("/prospect/{prospectId}")
    public ResponseEntity<List<InterestResponseDto>> getAllInterestByProspectId(@PathVariable Long prospectId){
        List<InterestResponseDto> interestResponseDtos = interestService.getAllInterestByCutomerId(prospectId);
        return ResponseEntity.ok(interestResponseDtos);
    }


    /**
     * Adds an interest to a customer.
     *
     * @param customerId The ID of the customer.
     * @param interestId The ID of the interest to add.
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/{customerId}/interests/{interestId}")
    public ResponseEntity<String> addInterestToCustomer(@PathVariable Long customerId, @PathVariable Long interestId) {
        interestService.addInterestToCustomer(customerId, interestId);
        return ResponseEntity.ok("Interest added successfully.");
    }

    /**
     * Removes an interest from a customer.
     *
     * @param customerId The ID of the customer.
     * @param interestId The ID of the interest to remove.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{customerId}/interests/{interestId}")
    public ResponseEntity<String> removeInterestFromCustomer(@PathVariable Long customerId, @PathVariable Long interestId) {
        interestService.removeInterestFromCustomer(customerId, interestId);
        return ResponseEntity.ok("Interest removed successfully.");
    }



}
