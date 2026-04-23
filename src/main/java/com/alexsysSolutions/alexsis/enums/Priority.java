package com.alexsysSolutions.alexsis.enums;

import lombok.Getter;

@Getter
public enum Priority {
    CRITICAL(7), // 7 min
    HIGH(60), // 1 hour
    MEDIUM(240), // 4 hours
    LOW(1440); // 1 day

    private final int delayInMinutes;

    Priority(int delayInMinutes) {
        this.delayInMinutes = delayInMinutes;
    }

}
