package com.alexsysSolutions.alexsis.config;

import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component


public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        boolean exists = userRepository.existsByRole(UserRole.SUPER_ADMIN);

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
        }
    }
}
