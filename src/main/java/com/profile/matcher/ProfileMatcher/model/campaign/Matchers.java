package com.profile.matcher.ProfileMatcher.model.campaign;

import lombok.Data;

@Data
public class Matchers {
    private Range level;
    private Has has;
    private DoesNotHave doesNotHave;
}
