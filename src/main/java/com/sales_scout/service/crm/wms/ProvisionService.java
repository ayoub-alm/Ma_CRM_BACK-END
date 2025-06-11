package com.sales_scout.service.crm.wms;


import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.ProvisionCreateDto;
import com.sales_scout.dto.response.crm.wms.ProvisionResponseDto;
import com.sales_scout.entity.Company;
import com.sales_scout.entity.crm.wms.Provision;
import com.sales_scout.entity.crm.wms.StockedItem;
import com.sales_scout.entity.crm.wms.StockedItemProvision;
import com.sales_scout.entity.crm.wms.need.StorageNeed;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.ProvisionMapper;
import com.sales_scout.repository.crm.wms.ProvisionRepository;
import com.sales_scout.repository.crm.wms.StockedItemProvisionRepository;
import com.sales_scout.repository.crm.wms.StockedItemRepository;
import com.sales_scout.repository.crm.wms.need.StorageNeedRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProvisionService {
    private final ProvisionRepository provisionRepository;
    private final StockedItemRepository stockedItemRepository;
    private final StockedItemProvisionRepository stockedItemProvisionRepository;
    private final StorageNeedRepository storageNeedRepository;

    public ProvisionService(ProvisionRepository provisionRepository, StockedItemRepository stockedItemRepository, StockedItemProvisionRepository stockedItemProvisionRepository, StorageNeedRepository storageNeedRepository) {
        this.provisionRepository = provisionRepository;
        this.stockedItemRepository = stockedItemRepository;
        this.stockedItemProvisionRepository = stockedItemProvisionRepository;
        this.storageNeedRepository = storageNeedRepository;
    }

    /**
     * This function allows to get all provision by company id
     * @param  companyId {Long} the ID of company
     * @return List<ProvisionResponseDto> list of provisions
     */
    public List<ProvisionResponseDto> getProvisionByCompanyId(Long companyId) {
        return this.provisionRepository.findAllByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(ProvisionMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * this function allows to create a new storage provision
     * @param provisionCreateDto data of provision to create
     * @return {ProvisionResponseDto} created provision
     */
    public ProvisionResponseDto createProvision(ProvisionCreateDto provisionCreateDto) {
        Provision provision =  this.provisionRepository.save(Provision.builder()
                .ref(UUID.randomUUID())
                .name(provisionCreateDto.getName())
                .unitOfMeasurement(provisionCreateDto.getUnitOfMeasurement())
                .company(Company.builder().id(provisionCreateDto.getCompanyId()).build())
                .initPrice(provisionCreateDto.getInitPrice())
                .itemOrder(provisionCreateDto.getOrder())
                .itemOrder(provisionCreateDto.getOrder())
                .build());
        return ProvisionMapper.toDto(provision);
    }

    /**
     * this function allows to update initial provisions
     * @param provisionCreateDto new provision data
     * @return ProvisionResponseDto  updated provision
     */
    public ProvisionResponseDto updateProvision(ProvisionCreateDto provisionCreateDto) {
        Provision provision = provisionRepository.findById(provisionCreateDto.getId())
                .orElseThrow(()-> new ResourceNotFoundException("provision not found with id " + provisionCreateDto.getId(), "", ""));

        provision.setName(provisionCreateDto.getName());
        provision.setUnitOfMeasurement(provisionCreateDto.getUnitOfMeasurement());
        provision.setInitPrice(provisionCreateDto.getInitPrice());
        provision.setItemOrder(provisionCreateDto.getOrder());
        provision.setIsStoragePrice(false);
        return ProvisionMapper.toDto(provisionRepository.save(provision));
    }

    /**
     * this function allows us to remove provision from stocked item
     * @param stockedItemId
     * @param provisionId
     * @return
     */
    public Boolean removeProvisionFromStockedItem(Long stockedItemId, Long provisionId) {
        try {
            StockedItem stockedItem = this.stockedItemRepository.findById(stockedItemId)
                    .orElseThrow(() -> new EntityNotFoundException("L'élément à stocker n'existe pas"));

            // Vérifier si stockedItemProvisions est nul pour éviter NullPointerException
            if (stockedItem.getStockedItemProvisions() == null || stockedItem.getStockedItemProvisions().isEmpty()) {
                throw new EntityNotFoundException("Aucune provision trouvée pour cet élément stocké.");
            }

            // Supprimer les provisions correspondantes
            stockedItem.getStockedItemProvisions().removeIf(stockedItemProvision ->
                    stockedItemProvision.getProvision().getId().equals(provisionId)
            );

            // Sauvegarder après suppression
            stockedItemRepository.save(stockedItem);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public Boolean addProvisionToStockedItem(Long stockedItemId, Long provisionId) {
        try {
            StockedItem stockedItem = stockedItemRepository.findById(stockedItemId)
                    .orElseThrow(() -> new EntityNotFoundException("L'élément à stocker n'existe pas"));

            Provision provision = provisionRepository.findById(provisionId)
                    .orElseThrow(() -> new EntityNotFoundException("La provision spécifiée n'existe pas"));

            // Vérifier si la liste des provisions est nulle, sinon l'initialiser
            if (stockedItem.getStockedItemProvisions() == null) {
                stockedItem.setStockedItemProvisions(new ArrayList<>());
            }

            List<StockedItemProvision> stockedItemProvisions = stockedItem.getStockedItemProvisions();

            // Vérifier si la provision est déjà présente
            boolean alreadyExists = stockedItemProvisions.stream()
                    .anyMatch(stockedItemProvision -> stockedItemProvision.getProvision().getId().equals(provisionId));

            if (alreadyExists) {
                throw new IllegalStateException("Cette provision est déjà ajoutée à cet élément stocké.");
            }

            // Ajouter la provision
            StockedItemProvision newStockedItemProvision = new StockedItemProvision();
            newStockedItemProvision.setStockedItem(stockedItem);
            newStockedItemProvision.setProvision(provision);

            stockedItemProvisions.add(newStockedItemProvision);

            StorageNeed storageNeed = StorageNeed.builder()
                    .id(stockedItem.getStorageNeed().getId())
                    .build();
            storageNeed.setUpdatedBy(SecurityUtils.getCurrentUser());
            storageNeed.setUpdatedAt(LocalDateTime.now());
            storageNeedRepository.save(storageNeed);


            // Sauvegarder l'objet mis à jour
            stockedItemRepository.save(stockedItem);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * this function allows to set provision as storage provision
     * @param provisionId provision Id
     * @return {ProvisionResponseDto} updated provision
     * @throws EntityNotFoundException if provision not found
     */
    public ProvisionResponseDto markProvisionAsStoragePrice(Long provisionId) throws EntityNotFoundException {
        Provision provision = provisionRepository.findById(provisionId)
                .orElseThrow(() -> new EntityNotFoundException("La provision spécifiée n'existe pas"));

        // Fetch all provisions of the same company (not deleted)
        List<Provision> companyProvisions = provisionRepository.findAllByCompanyIdAndDeletedAtIsNull(provision.getCompany().getId());
        // Set all to false
        for (Provision p : companyProvisions) {
            p.setIsStoragePrice(false);
        }
        // Set the specified one to true
        provision.setIsStoragePrice(true);
        // Save all updated provisions
        companyProvisions.add(provision); // Ensure the target one is included
        provisionRepository.saveAll(companyProvisions);
        return ProvisionMapper.toDto(provision);
    }

}
