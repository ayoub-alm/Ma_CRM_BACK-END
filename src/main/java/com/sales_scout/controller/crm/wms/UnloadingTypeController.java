package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.response.crm.wms.UnloadingTypeResponseDto;
import com.sales_scout.service.crm.wms.UnloadingTypeService;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/unloading-types")
public class UnloadingTypeController {

    private final UnloadingTypeService unloadingTypeService;

    public UnloadingTypeController(UnloadingTypeService unloadingTypeService) {
        this.unloadingTypeService = unloadingTypeService;
    }

    /**
     * Get all unloading types by company ID
     * @param companyId the ID of the company
     * @return list of unloading types
     */
    @GetMapping("")
    public ResponseEntity<List<UnloadingTypeResponseDto>> getAllUnloadingTypeByCompanyId(@RequestParam Long companyId){
        return ResponseEntity.ok( this.unloadingTypeService.getUnloadingTypesByCompanyId(companyId) );
    }
}
