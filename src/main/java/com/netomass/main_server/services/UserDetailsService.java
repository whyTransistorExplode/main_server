package com.netomass.main_server.services;

import com.netomass.main_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsService {
   private final UserRepository userRepository;


    public UserDetails FindUserByUsername(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails FindUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }
}
