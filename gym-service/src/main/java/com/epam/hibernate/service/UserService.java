package com.epam.hibernate.service;

import com.epam.hibernate.config.PasswordConfig;
import com.epam.hibernate.entity.RoleEnum;
import com.epam.hibernate.entity.User;
import com.epam.hibernate.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserJpaRepository userRepository;

    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveAdmin() {
        User admin = new User();
        String password = PasswordConfig.passwordEncoder().encode("admin");
        admin.setActive(true);
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setUsername("admin");
        admin.setPassword(password);
        admin.setRole(RoleEnum.ADMIN);
        userRepository.save(admin);
    }
}
