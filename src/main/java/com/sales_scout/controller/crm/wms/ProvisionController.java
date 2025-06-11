package com.sales_scout.controller.crm.wms;


import com.sales_scout.dto.request.create.wms.ProvisionCreateDto;
import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.service.crm.wms.ProvisionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
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
        throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(this.provisionService.createProvision(provisionCreateDto));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(), e.getCause().getMessage(),"");
        }
    }

    @GetMapping("/mark-provision-as-storage-price/{provisionId}")
    public ResponseEntity<ProvisionResponseDto> markProvisionAsStoragePrice(@PathVariable Long provisionId )
            throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(this.provisionService.markProvisionAsStoragePrice(provisionId));
        }catch (Exception e){
            throw new ResourceNotFoundException(e.getMessage(), e.getCause().getMessage(),"");
        }
    }

    /**
     * this end point allows to update initial provision
     * @param provisionCreateDto date for update
     * @return {ProvisionResponseDto} updated provision
     * @throws ResourceNotFoundException If provision to update is not found
     */
    @PutMapping("")
    public ResponseEntity<ProvisionResponseDto> updateProvision(@RequestBody ProvisionCreateDto provisionCreateDto)
            throws ResourceNotFoundException{
        try {
            return ResponseEntity.ok(this.provisionService.updateProvision(provisionCreateDto));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage(),  e.getCause().getMessage(), "provision not found with Id " + provisionCreateDto.getId());
        }
    }


    @DeleteMapping("/stocked-item/{stockedItemId}/provision/{provisionId}")
    public ResponseEntity<Boolean> removeProvisionFromStockedItem(@PathVariable Long stockedItemId,
                                                                  @PathVariable Long provisionId) {
        try {
            boolean isRemoved = provisionService.removeProvisionFromStockedItem(stockedItemId, provisionId);
            if (isRemoved) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/add-to-stocked-item/{stockedItemId}/provision/{provisionId}")
    public ResponseEntity<Boolean> addProvisionToStockedItem(@PathVariable Long stockedItemId,
                                                            @PathVariable Long provisionId) {
        try {
          return  ResponseEntity.ok( provisionService.addProvisionToStockedItem(stockedItemId, provisionId));
        } catch (EntityNotFoundException | IllegalStateException e) {
          throw new EntityNotFoundException(e.getMessage(),e);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage(), e.getMessage(),e);
        }
    }
}
