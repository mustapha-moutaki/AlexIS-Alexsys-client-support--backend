package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.service.IEmailTemplateService;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService implements IEmailTemplateService {
    public EmailTemplateService(){

    }

    @Override
    public String buildWelcomeEmailTemplate(String username) {
        return "Dear " + username + ",\n\n" +
                "Welcome to our service! We're excited to have you on board.\n\n" +
                "Best regards,\n" +
                "AlexIS Team";
    }

    @Override
    public String buildAdminNotificationTemplate(String username, String email, UserRole role) {
        return "Admin Notification:\n\n" +
                "You just created new user " + username + " successfully.\n\n" +
                "Email " + email + ".\n\n" +
                "Role " + role + ".\n\n" +
                "Best regards,\n" +
                "The System";
    }
}
