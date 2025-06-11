package com.sales_scout.service.crm.wms.invoice;

import com.sales_scout.dto.request.create.wms.StorageInvoicePaymentRequestDto;
import com.sales_scout.entity.crm.wms.invoice.InvoicePayment;
import com.sales_scout.entity.crm.wms.invoice.Payment;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoice;
import com.sales_scout.entity.crm.wms.invoice.StorageInvoiceStatus;
import com.sales_scout.exception.ResourceNotFoundException;
import com.sales_scout.repository.crm.wms.invoice.InvoicePaymentRepository;
import com.sales_scout.repository.crm.wms.invoice.StorageInvoiceRepository;
import com.sales_scout.repository.crm.wms.invoice.StoragePaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StoragePaymentService {
    private final StorageInvoiceRepository storageInvoiceRepository;
    private final StoragePaymentRepository storagePaymentRepository;
    private final InvoicePaymentRepository invoicePaymentRepository;
    public StoragePaymentService(StorageInvoiceRepository storageInvoiceRepository, StoragePaymentRepository storagePaymentRepository, InvoicePaymentRepository invoicePaymentRepository) {
        this.storageInvoiceRepository = storageInvoiceRepository;
        this.storagePaymentRepository = storagePaymentRepository;
        this.invoicePaymentRepository = invoicePaymentRepository;
    }

    public Payment addPaymentToInvoice(StorageInvoicePaymentRequestDto storageInvoicePaymentRequestDto){
        StorageInvoice storageInvoice = this.storageInvoiceRepository.findById(storageInvoicePaymentRequestDto.getInvoiceId())
                .orElseThrow(()-> new ResourceNotFoundException("Invoice not found with id" + storageInvoicePaymentRequestDto.getInvoiceId(), "", ""));

        Payment payment = this.storagePaymentRepository.save(
                Payment.builder()
                        .ref(storageInvoicePaymentRequestDto.getRef())
                        .amount(storageInvoicePaymentRequestDto.getAmount())
                        .paymentMethod(storageInvoicePaymentRequestDto.getPaymentMethod())
                        .build());
        storagePaymentRepository.flush();
        invoicePaymentRepository.save(InvoicePayment.builder()
                        .storageInvoice(storageInvoice)
                        .payment(payment)
                .build());
//         #TODO change status of invoice if payment is completed
        List<InvoicePayment> invoicePayments = this.invoicePaymentRepository.findByStorageInvoiceId(storageInvoice.getId());
        AtomicReference<Double> totalPaymentAmount = new AtomicReference<>(0.00);
        invoicePayments.forEach(invoicePayment -> {
            totalPaymentAmount.updateAndGet(v -> v + invoicePayment.getPayment().getAmount());
        });
        if (totalPaymentAmount.get() >= storageInvoice.getTotalTtc()){
            storageInvoice.setStatus(StorageInvoiceStatus.builder().id(4L).build());
        }
        return payment;
    }
}
