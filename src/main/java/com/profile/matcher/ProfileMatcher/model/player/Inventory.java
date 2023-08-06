package com.profile.matcher.ProfileMatcher.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Inventory {
    private int cash;
    private int coins;

    @ElementCollection
    @CollectionTable(name = "player_items", joinColumns = @JoinColumn(name = "player_id"))
    @MapKeyColumn(name = "item_name")
    @Column(name = "quantity")
    private Map<String, Integer> items;
}
