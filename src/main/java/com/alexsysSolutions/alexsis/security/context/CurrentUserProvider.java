package com.alexsysSolutions.alexsis.security.context;

import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("User not authenticated");
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public Long getUserId() {
        return getCurrentUser().getUser().getId();
    }

    public String getEmail() {
        return getCurrentUser().getUsername();
    }
    public UserRole getRole(){
        return getCurrentUser().getUser().getRole();
    }
}