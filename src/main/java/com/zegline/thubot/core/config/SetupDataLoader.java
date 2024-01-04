package com.zegline.thubot.core.config;

import com.zegline.thubot.core.model.security.Privilege;
import com.zegline.thubot.core.model.security.Role;
import com.zegline.thubot.core.model.security.User;
import com.zegline.thubot.core.repository.PrivilegeRepository;
import com.zegline.thubot.core.repository.RoleRepository;
import com.zegline.thubot.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        if (userRepository.existsByUsername("test_sys")) {
            return;
        }
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> sysPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_SYS", sysPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role sysRole = roleRepository.findByName("ROLE_SYS");

        User user = new User();
        user.setUsername("test_sys");

        user.setPassword(passwordEncoder.encode("xenopower"));
        user.setRoles(Arrays.asList(sysRole));
        userRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
