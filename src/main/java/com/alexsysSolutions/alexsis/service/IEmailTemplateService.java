package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.enums.UserRole;

public interface IEmailTemplateService {
    public String buildWelcomeEmailTemplate(String username);
    public String buildAdminNotificationTemplate(String username, String email, UserRole role);
}
