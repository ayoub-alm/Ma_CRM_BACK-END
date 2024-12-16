package com.sales_scout.service.data;


import com.sales_scout.entity.data.DeliveryMethod;
import com.sales_scout.repository.data.DeliveryMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryMethodService {
    private final DeliveryMethodRepository deliveryMethodRepository;

    public DeliveryMethodService(DeliveryMethodRepository deliveryMethodRepository) {
        this.deliveryMethodRepository = deliveryMethodRepository;
    }

    /**
     * This function allows to get delivery methods
     * @return {List<DeliveryMethod>}
     */
    public List<DeliveryMethod> getAllDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    /**
     * this function allows us to get delivery method by id
     * @param id {Long} The ID of delivery method
     * @return {DeliveryMethod}
     */
    public Optional<DeliveryMethod> getDeliveryMethodById(Long id) {
        return deliveryMethodRepository.findById(id);
    }

    /**
     * This function allows to save new delivery method
     * @param {DeliveryMethod} the delivery method to save
     * @return {DeliveryMethod} saved delivery method
     */
    public DeliveryMethod saveDeliveryMethod(DeliveryMethod deliveryMethod) {
        return deliveryMethodRepository.save(deliveryMethod);
    }

    /**
     * This function allows to delete delivery method with giving ID
     * @param id {Long}  the id of delivery method to delete
     */
    public void deleteDeliveryMethod(Long id) {
        deliveryMethodRepository.deleteById(id);
    }

    /**
     * This function allows to  update delivery
     * @param {deliveryMethod}
     * @return Updated delivery
     */
    public DeliveryMethod updateDeliveryMethod(DeliveryMethod deliveryMethod) {
        return deliveryMethodRepository.save(deliveryMethod);
    }
}
