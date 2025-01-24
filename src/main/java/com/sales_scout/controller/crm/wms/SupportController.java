package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.response.crm.wms.SupportResponseDto;
import com.sales_scout.entity.crm.wms.Support;
import com.sales_scout.service.crm.wms.SupportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/supports")
public class SupportController {
    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @GetMapping("")
    public ResponseEntity<List<SupportResponseDto>> getAllSupportsByCompanyId(@RequestParam Long companyId) throws Exception {
        try {
          return   ResponseEntity.ok(this.supportService.getAllSupportByCompanyId(companyId));
        } catch (Exception e) {
            // Return 500 Internal Server Error with an empty list or appropriate error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
}
