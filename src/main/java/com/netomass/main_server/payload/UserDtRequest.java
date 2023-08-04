package com.netomass.main_server.payload;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDtRequest {
    private String username;
    private String password;
}

