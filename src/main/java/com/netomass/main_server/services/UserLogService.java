package com.netomass.main_server.services;


import com.netomass.main_server.entity.User;
import com.netomass.main_server.entity.UserLog;
import com.netomass.main_server.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogService {
    private final UserLogRepository userLogRepository;

    public void userAction(User user, String message){
        UserLog userLog = UserLog.builder()
                .currentUser(user)
                .message(message)
                .build();
        userLogRepository.save(userLog);
    }
}
