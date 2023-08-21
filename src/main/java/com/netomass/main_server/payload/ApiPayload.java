package com.netomass.main_server.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiPayload {
    private String message;
    private Object content;
    @Builder.Default
    private boolean isSuccess = true;
}
