package com.netomass.main_server.data;

import com.netomass.main_server.entity.User;
import com.netomass.main_server.enums.Role;
import com.netomass.main_server.payload.PathPayload;
import com.netomass.main_server.repository.UserRepository;
import com.netomass.main_server.services.PathService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PathService pathService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterSetup(){

        if (!ddl.startsWith("create"))
            return;


        User user = User.builder().email("mailgmailby@gmail.com")
                .name("robert")
                .password(passwordEncoder.encode("wolf"))
                .userRole(Role.GUEST)
                .build();

        userRepository.save(user);


        user = User.builder().email("gammailby@gmail.com")
                .name("stark")
                .password(passwordEncoder.encode("ironman"))
                .userRole(Role.USER)
                .build();

        userRepository.save(user);


        user = User.builder().email("asdailgmailby@gmail.com")
                .name("thor")
                .password(passwordEncoder.encode("lightning"))
                .userRole(Role.HOME_USER)
                .build();

        userRepository.save(user);

        user = User.builder().email("ulug0905by@gmail.com")
                .name("ulugbek")
                .password(passwordEncoder.encode("mypassword"))
                .userRole(Role.ADMIN)
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findByName(user.getName()).orElseThrow(() -> new ResourceNotFoundException("not found"));

        //################# paths add
        PathPayload path = PathPayload.builder()
                .name("Disk_C")
                .path("C:/")
                .userId(savedUser.getId())
                .build();

        pathService.addPath(path, savedUser.getId());

        path = PathPayload.builder()
                .name("disk_D")
                .path("D:/ENG")
                .userId(savedUser.getId())
                .build();

        pathService.addPath(path, savedUser.getId());
    }
}
