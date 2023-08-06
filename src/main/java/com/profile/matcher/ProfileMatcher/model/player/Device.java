package com.profile.matcher.ProfileMatcher.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Device {
    private String id;
    private String model;
    private String carrier;
    private String firmware;
}
