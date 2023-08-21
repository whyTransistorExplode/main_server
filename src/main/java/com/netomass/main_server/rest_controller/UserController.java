package com.netomass.main_server.rest_controller;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.UserDtRequest;
import com.netomass.main_server.payload.UserPayload;
import com.netomass.main_server.services.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
public class UserController {

    private UserDetailsService userService;

    @GetMapping("/get-user")
    public ResponseEntity<UserPayload> getUser(){
        UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) userToken.getPrincipal();
        UserPayload payloadUser = UserPayload.builder()
                .name(userDetails.getUsername())
                .email(userDetails.getEmail())
//                .userRole(user.getAuthorities().)
                .build();
        return ResponseEntity.ok(payloadUser);
    }

    @GetMapping("/get-user-list")
    @Secured({"ADMIN"})
    public ResponseEntity<ApiPayload> getUserList(){
        ApiPayload apiPayload = userService.getUsers();
        return  ResponseEntity.ok(apiPayload);
    }

    @GetMapping("/get-user-by-id/{id}")
    @Secured({"ADMIN"})
    public ResponseEntity<ApiPayload> getUserById(@PathVariable Long id){
        ApiPayload apiPayload = userService.getUserPayloadById(id);
        return  ResponseEntity.ok(apiPayload);
    }


    @PostMapping("/add-user")
    @Secured({"ADMIN"})
    public ResponseEntity<ApiPayload> addUser(@RequestBody UserPayload userPayload){

        ApiPayload apiPayload = userService.saveUser(userPayload);
        return  ResponseEntity.ok(apiPayload);
    }
}
