package com.sales_scout.controller.crm.wms;


import com.sales_scout.dto.request.create.wms.ProvisionCreateDto;
import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.ProvisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * This end point allows to create new storage provision
     * @param provisionCreateDto data of provision to create
     * @return { ResponseEntity<ProvisionResponseDto> } created provision
     * @throws Exception run time exception
     */
    @PostMapping("")
    public ResponseEntity<ProvisionResponseDto> createProvision(@RequestBody ProvisionCreateDto provisionCreateDto)
        throws Exception{
        try {
            return ResponseEntity.ok(this.provisionService.createProvision(provisionCreateDto));
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

}
