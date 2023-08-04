package com.netomass.main_server.data;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.enums.Role;
import com.netomass.main_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterSetup(){
        User user = User.builder().email("mailgmailby@gmail.com")
                .name("robert")
                .password(passwordEncoder.encode("nowayhomeno"))
                .userRole(Role.GUEST)
                .build();

        userRepository.save(user);


        user = User.builder().email("gammailby@gmail.com")
                .name("robert")
                .password(passwordEncoder.encode("unprotected_asdf"))
                .userRole(Role.GUEST)
                .build();

        userRepository.save(user);


        user = User.builder().email("asdailgmailby@gmail.com")
                .name("robert")
                .password(passwordEncoder.encode("unprotected_password"))
                .userRole(Role.GUEST)
                .build();

        userRepository.save(user);

        user = User.builder().email("ulug0905by@gmail.com")
                .name("ulugbek")
                .password(passwordEncoder.encode("mypassword"))
                .userRole(Role.ADMIN)
                .build();

        userRepository.save(user);
    }
}
