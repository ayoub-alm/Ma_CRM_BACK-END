package com.sales_scout.controller.data;


import com.sales_scout.entity.data.CompanySize;
import com.sales_scout.service.data.CompanySizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/company-size")
public class CompanySizeController {
    private final CompanySizeService companySizeService;
    public CompanySizeController(CompanySizeService companySizeService) {
        this.companySizeService = companySizeService;
    }

    /**
     * Get all company sizes
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<CompanySize>> getCompanySize() {
        List<CompanySize> companySizes = companySizeService.getAllCompanySize();
        return ResponseEntity.ok(companySizes);
    }
}
