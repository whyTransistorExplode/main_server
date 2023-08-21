package com.netomass.main_server.repository;

import com.netomass.main_server.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {

}
