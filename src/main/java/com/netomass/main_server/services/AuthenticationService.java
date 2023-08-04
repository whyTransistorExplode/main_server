package com.netomass.main_server.services;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.payload.AuthenticationResponse;
import com.netomass.main_server.payload.UserDtRequest;
import com.netomass.main_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(UserDtRequest userDtRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDtRequest.getUsername(),
                        userDtRequest.getPassword()
                )
        );

        User user = userRepository.findByName(userDtRequest.getUsername()).orElseThrow();
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
