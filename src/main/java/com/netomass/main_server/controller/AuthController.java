package com.netomass.main_server.controller;


import com.netomass.main_server.payload.AuthenticationResponse;
import com.netomass.main_server.payload.UserDtRequest;
import com.netomass.main_server.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody UserDtRequest userDtRequest){

        return ResponseEntity.ok(service.authenticate(userDtRequest));
    }
}
