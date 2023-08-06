package com.profile.matcher.ProfileMatcher.model.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", unique = true)
    @JsonProperty("player_id")
    private String playerId;

    @Column
    private String credential;

    @Column
    private String created;

    @Column
    private String modified;

    @Column(name = "last_session")
    @JsonProperty("last_session")
    private String lastSession;

    @Column(name = "total_spent")
    @JsonProperty("total_spent")
    private int totalSpent;

    @Column(name = "total_refund")
    @JsonProperty("total_refund")
    private int totalRefund;

    @Column(name = "total_transactions")
    @JsonProperty("total_transactions")
    private int totalTransactions;

    @Column(name = "last_purchase")
    @JsonProperty("last_purchase")
    private String lastPurchase;

    @Column
    private int level;

    @Column
    private int xp;

    @Column(name = "total_playtime")
    @JsonProperty("total_playtime")
    private int totalPlaytime;

    @Column
    private String country;

    @Column
    private String language;

    @Column
    private String birthdate;

    @Column
    private String gender;

    @Column(name = "_customfield")
    @JsonProperty("_customfield")
    private String customField;

    @Column(name = "active_campaigns")
    @JsonProperty("active_campaigns")
    @ElementCollection
    private List<String> activeCampaigns = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "player_devices", joinColumns = @JoinColumn(name = "player_id"))
    private List<Device> devices;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "clan_id")),
            @AttributeOverride(name = "name", column = @Column(name = "clan_name"))
    })
    private Clan clan;

    @Embedded
    private Inventory inventory;
}