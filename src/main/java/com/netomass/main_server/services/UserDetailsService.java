package com.netomass.main_server.services;

import com.netomass.main_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDetailsService {
    @Autowired
    UserRepository userRepository;
}
