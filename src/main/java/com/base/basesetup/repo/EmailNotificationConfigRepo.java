package com.base.basesetup.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.EmailNotificationConfigVO;

public interface EmailNotificationConfigRepo  extends JpaRepository<EmailNotificationConfigVO, Long> {

    Optional<EmailNotificationConfigVO>
        findByModuleCodeAndIsActive(String moduleCode, String isActive);
}
