package com.zegline.thubot.core.repository;

import com.zegline.thubot.core.model.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
