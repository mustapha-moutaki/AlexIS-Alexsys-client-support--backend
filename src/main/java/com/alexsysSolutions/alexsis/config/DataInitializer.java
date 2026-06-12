package com.alexsysSolutions.alexsis.config;

import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component


public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        boolean exists = userRepository.existsByRole(UserRole.SUPER_ADMIN);
        boolean existsAdminByEmail = userRepository.existsByEmail("jhonDoe@gmail.com");
        if(!exists){
            User superAdmin = User.builder()
                    .username("superadmin")
                    .firstName("superadmin")
                    .lastName("system")
                    .email("superadmin@gmail.com")
                    .password(PasswordUtil.hash("superadminpassword"))
                    .role(UserRole.SUPER_ADMIN)
                    .updatedAt(LocalDateTime.now())
                    .createdBy("system-auto-run")
                    .build();

            userRepository.save(superAdmin);
            logger.info("Super admin user created with email: {}", superAdmin.getEmail());
        }

        if (!existsAdminByEmail) {
            User adminUser = User.builder()
                    .username("jhondoe")
                    .firstName("John")
                    .lastName("Doe")
                    .email("jhonDoe@gmail.com")
                    .password(PasswordUtil.hash("password")) // Hashing the password as per your utility
                    .role(UserRole.ADMIN)
                    .updatedAt(LocalDateTime.now())
                    .createdBy("system-auto-run")
                    .build();

            userRepository.save(adminUser);
            logger.info("Admin user created with email: {}", adminUser.getEmail());
    }
}}
