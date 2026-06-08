package com.alexsysSolutions.alexsis.enums;

public enum AiPlan {
    GUEST(3),
    USER(7);

    public final int limit;

    AiPlan(int limit){
       this.limit =limit;
    }
}
