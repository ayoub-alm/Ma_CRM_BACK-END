package com.sales_scout.controller.crm.wms;

import com.sales_scout.dto.response.crm.wms.SupportResponseDto;
import com.sales_scout.dto.response.crm.wms.TemperatureResponseDto;
import com.sales_scout.service.crm.wms.SupportService;
import com.sales_scout.service.crm.wms.TemperatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/temperatures")
public class TemperatureController {
    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }


    @GetMapping("")
    public ResponseEntity<List<TemperatureResponseDto>> getAllTemperatureByCompanyId(@RequestParam Long companyId) throws Exception {
        try {
          return   ResponseEntity.ok(this.temperatureService.getAllTemperatureByCompanyId(companyId));
        } catch (Exception e) {
            // Return 500 Internal Server Error with an empty list or appropriate error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
}
