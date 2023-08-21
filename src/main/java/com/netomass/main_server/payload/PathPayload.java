package com.netomass.main_server.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathPayload {
    private String path;
    private String name;
    private Long userId;
}
