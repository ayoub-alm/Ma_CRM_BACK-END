package com.sales_scout.service.crm.wms.invoice;

import com.sales_scout.Auth.SecurityUtils;
import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentUpdateRequestDto;
import com.sales_scout.dto.response.crm.wms.StorageInvoicePaymentResponseDto;
import com.sales_scout.entity.crm.wms.invoice.InvoicePayment;
import com.sales_scout.entity.crm.wms.invoice.Payment;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStatus;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.mapper.wms.StorageInvoicePaymentMapper;
import com.sales_scout.repository.crm.wms.invoice.InvoicePaymentRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import com.sales_scout.repository.crm.wms.invoice.StoragePaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StoragePaymentService {
    private final StorageInvoiceRepository storageInvoiceRepository;
    private final StoragePaymentRepository storagePaymentRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;
    private final StorageInvoicePaymentMapper storageInvoicePaymentMapper;
    public StoragePaymentService(StorageInvoiceRepository storageInvoiceRepository, StoragePaymentRepository storagePaymentRepository, InvoicePaymentRepository invoicePaymentRepository, StorageInvoicePaymentMapper storageInvoicePaymentMapper) {
        this.storageInvoiceRepository = storageInvoiceRepository;
        this.storagePaymentRepository = storagePaymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
        this.storageInvoicePaymentMapper = storageInvoicePaymentMapper;
    }

    /**
     *
     * @param companyId
     * @return
     */
    public List<StorageInvoicePaymentResponseDto> getAllPaymentsByCompanyId(Long companyId){
        return this.invoicePaymentRepository.findByStorageInvoice_StorageContract_Company_Id(companyId)
                .stream()
                .map(InvoicePayment::getPayment)
                .map(storageInvoicePaymentMapper::toResponseDto)
                .toList();
    }

    /**
     * This function allows to get payment by Id
     * @param paymentId the payment ID
     * @return StorageInvoicePaymentResponseDto
     * @throws ResourceNotFoundException in case of payment not found with the giving ID
     */
    public StorageInvoicePaymentResponseDto getPaymentsById(Long paymentId) throws ResourceNotFoundException{
        return this.storagePaymentRepository.findById(paymentId)
                .map(storageInvoicePaymentMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Payment ot found with id "+ paymentId,"",""));
    }



    /**
     *
     * @param storageInvoicePaymentRequestDto
     * @return
     */
    public Payment addPaymentToInvoice(StorageInvoicePaymentRequestDto storageInvoicePaymentRequestDto){
        StorageInvoice storageInvoice = this.storageInvoiceRepository.findById(storageInvoicePaymentRequestDto.getInvoiceId())
                .orElseThrow(()-> new ResourceNotFoundException("Invoice not found with id" + storageInvoicePaymentRequestDto.getInvoiceId(), "", ""));
        Payment paymentToSave =   Payment.builder()
                .ref(storageInvoicePaymentRequestDto.getRef())
                .amount(storageInvoicePaymentRequestDto.getAmount())
                .paymentMethod(storageInvoicePaymentRequestDto.getPaymentMethod())
                .receptionDate(storageInvoicePaymentRequestDto.getReceptionDate())
                .build();
        paymentToSave.setCreatedBy(SecurityUtils.getCurrentUser());
        paymentToSave.setUpdatedBy(SecurityUtils.getCurrentUser());
        Payment payment = this.storagePaymentRepository.save(paymentToSave);
        storagePaymentRepository.flush();
        InvoicePayment invoicePayment1 = InvoicePayment.builder()
                .storageInvoice(storageInvoice)
                .payment(payment)
                .build();
        invoicePayment1.setCreatedBy(SecurityUtils.getCurrentUser());
        invoicePayment1.setUpdatedBy(SecurityUtils.getCurrentUser());
        invoicePaymentRepository.save(invoicePayment1);

        List<InvoicePayment> invoicePayments = this.invoicePaymentRepository.findByStorageInvoiceId(storageInvoice.getId());
        AtomicReference<Double> totalPaymentAmount = new AtomicReference<>(0.00);
        invoicePayments.forEach(invoicePayment -> {
            totalPaymentAmount.updateAndGet(v -> v + invoicePayment.getPayment().getAmount());
        });
        if (totalPaymentAmount.get() >= storageInvoice.getTotalTtc()){
            storageInvoice.setStatus(StorageInvoiceStatus.builder().id(3L).build());
        }
        return payment;
    }

    /**
     *
     * @param paymentId
     * @param updateRequestDto
     * @return
     */
    public Payment validatePayment(Long paymentId, StorageInvoicePaymentUpdateRequestDto updateRequestDto){
        Payment payment = this.storagePaymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment not found ","",""));

        payment.setValidationDate(LocalDateTime.now());
        payment.setValidationStatus(true);

        payment.setUpdatedBy(SecurityUtils.getCurrentUser());
        return storagePaymentRepository.save(payment);
    }
}
