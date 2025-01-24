package com.sales_scout.controller.crm.wms;


import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.service.crm.wms.ProvisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provisions")
public class ProvisionController {
    private final ProvisionService provisionService;

    public ProvisionController(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProvisionResponseDto>> getAllProvisionByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok(this.provisionService.getProvisionByCompanyId(companyId));
    }
}
