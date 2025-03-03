package com.sales_scout.dto.error;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Setter @Getter
public class ResponseError {
    public String message;
    public int code;
}
