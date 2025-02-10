package com.sales_scout.dto.response.crm.wms;

import com.sales_scout.entity.crm.wms.Dimension;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter @Setter
public class SupportResponseDto {
    private Long id;
    private String name;
    private String ref;
    private Dimension dimension;
}
