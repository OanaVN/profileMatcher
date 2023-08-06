package com.profile.matcher.ProfileMatcher.model.campaign;

import lombok.Data;

@Data
public class Campaign {
    private String game;
    private String name;
    private double priority;
    private String startDate;
    private String endDate;
    private Matchers matcher;
    private boolean enabled;
    private String lastUpdated;
}
