package com.sales_scout.controller.crm.wms;

import com.sales_scout.service.crm.wms.StockedItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocked-items")
public class StockedItemController {
    private final StockedItemService stockedItemService;

    public StockedItemController(StockedItemService stockedItemService) {
        this.stockedItemService = stockedItemService;
    }



    @DeleteMapping("{stockedItemId}")
    public ResponseEntity<Boolean> deleteStockedItem(@PathVariable Long stockedItemId) throws TransactionSystemException {
        try {
            return ResponseEntity.ok(this.stockedItemService.deleteStockedItem(stockedItemId));
        }catch (TransactionSystemException e){
            throw  new TransactionSystemException(e.getMessage());
        }
    }
}
