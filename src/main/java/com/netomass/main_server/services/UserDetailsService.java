package com.netomass.main_server.services;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.enums.Role;
import com.netomass.main_server.payload.ApiPayload;
import com.netomass.main_server.payload.UserPayload;
import com.netomass.main_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsService {
    private final UserRepository userRepository;


    public UserDetails FindUserByUsername(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails FindUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }


    public ApiPayload saveUser(UserPayload payload) {

        if (payload.getEmail() == null
                || payload.getEmail().length() < 3
                || payload.getName() == null
                || payload.getName().length() < 3
                || payload.getPassword() == null
                || payload.getPassword().length() < 4
        )
            return ApiPayload.builder().message("bad credentials").build();

        try {
            User newUser = User.builder()
                    .email(payload.getEmail())
                    .userRole(Role.valueOf(payload.getRole()))
                    .name(payload.getName())
                    .password(payload.getPassword())
                    .build();

            userRepository.save(newUser);

            return ApiPayload.builder().message("success").build();
        }catch (IllegalArgumentException e){
            return ApiPayload.builder().message("bad credentials: " + e.getCause()).build();
        }

    }

    public ApiPayload getUsers() {
        List<User> all = userRepository.findAll();

        final List<UserPayload> sendAbleUsers = new ArrayList<>();

        all.forEach(
                (user) ->
                sendAbleUsers.add(UserPayload.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .role(user.getUserRole().toString())
                        .build())
        );
        return ApiPayload.builder().content(sendAbleUsers).message("list users").build();
    }

    public ApiPayload getUserPayloadById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("not found"));
        UserPayload userPayload = UserPayload.builder()
                .role(user.getUserRole().name())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return ApiPayload.builder().content(userPayload).build();
    }
    public ApiPayload getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("not found"));

        return ApiPayload.builder().content(user).build();
    }


}
