    package com.sales_scout.mapper.wms;

    import com.sales_scout.dto.response.crm.wms.StorageCreditNoteResponseDto;
    import com.sales_scout.entity.crm.wms.assets.StorageCreditNote;
    import com.sales_scout.mapper.UserMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;

    @Component
    @RequiredArgsConstructor
    public class StorageCreditNoteMapper {
        private final StorageInvoiceMapper storageInvoiceMapper;
        private final UserMapper userMapper;
        public StorageCreditNoteResponseDto toResponseDto(StorageCreditNote storageCreditNote){

            StorageCreditNoteResponseDto storageCreditNoteResponseDto = StorageCreditNoteResponseDto.builder()
                    .id(storageCreditNote.getId())
                    .number(storageCreditNote.getNumber())
                    .totalHt(storageCreditNote.getTotalHt())
                    .totalTtc(storageCreditNote.getTotalTtc())
                    .tva(storageCreditNote.getTva())
                    .sendDate(storageCreditNote.getSendDate())
                    .sendStatus(storageCreditNote.getSendStatus())
                    .returnDate(storageCreditNote.getReturnDate())
                    .returnStatus(storageCreditNote.getReturnStatus())
                    .status(storageCreditNote.getStatus())
                    .storageInvoice(storageInvoiceMapper.toResponse(storageCreditNote.getStorageInvoice()))
                    .build();

            storageCreditNoteResponseDto.setId(storageCreditNote.getId());
            storageCreditNoteResponseDto.setCreatedAt(storageCreditNote.getCreatedAt());
            storageCreditNoteResponseDto.setUpdatedAt(storageCreditNote.getUpdatedAt());
            storageCreditNoteResponseDto.setCreatedBy(userMapper.fromEntity(storageCreditNote.getCreatedBy()));
            storageCreditNoteResponseDto.setUpdatedBy(userMapper.fromEntity(storageCreditNote.getUpdatedBy()));
            return storageCreditNoteResponseDto;
        }
    }
