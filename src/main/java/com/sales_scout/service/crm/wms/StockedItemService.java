package com.sales_scout.service.crm.wms;

import com.sales_scout.repository.crm.wms.StockedItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public class StockedItemService {
    private final StockedItemRepository stockedItemRepository;

    public StockedItemService(StockedItemRepository stockedItemRepository) {
        this.stockedItemRepository = stockedItemRepository;
    }


    public boolean deleteStockedItem(Long stockedItemId) throws TransactionSystemException{
        try{
          this.stockedItemRepository.deleteById(stockedItemId);
          return true;
        }catch (TransactionSystemException e){
            throw new TransactionSystemException(e.getMessage());
        }
    }
}
