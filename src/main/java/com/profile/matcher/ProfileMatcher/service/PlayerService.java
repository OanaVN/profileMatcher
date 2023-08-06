package com.profile.matcher.ProfileMatcher.service;

import com.profile.matcher.ProfileMatcher.model.campaign.*;
import com.profile.matcher.ProfileMatcher.model.player.Inventory;
import com.profile.matcher.ProfileMatcher.model.player.Player;
import com.profile.matcher.ProfileMatcher.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> getPlayerByPlayerId(String playerId) {
        Optional<Player> playerOptional  = playerRepository.findByPlayerId(playerId);
        Campaign currentCampaign = getCurrentCampaign();
        playerOptional.ifPresent(player -> {
            if (!player.getActiveCampaigns().contains(currentCampaign.getName())) {
                Matchers campaignMatchers = currentCampaign.getMatcher();
                if ((playerMatchesConditions(player, campaignMatchers))) {
                    player.getActiveCampaigns().add(currentCampaign.getName());
                    playerRepository.save(player);
                }
            }
        });
        return playerOptional;
    }

    private boolean playerMatchesConditions(Player player, Matchers campaignMatchers) {
        boolean levelMatch = !matchLevelUser(player, campaignMatchers);
        boolean countryOrItemsMatch = !matchCountryOrItemsUser(player, campaignMatchers);
        boolean notHaveItemsMatch = !matchNotHaveItemsUser(player, campaignMatchers);
        return levelMatch && countryOrItemsMatch && notHaveItemsMatch;
    }

    private boolean matchLevelUser(Player player, Matchers campaignMatchers) {
        if (campaignMatchers.getLevel() != null) {
            Range levelRange = campaignMatchers.getLevel();
            int playerLevel = player.getLevel();
            return playerLevel < levelRange.getMin() || playerLevel > levelRange.getMax();
        }
        return false;
    }

    private boolean matchCountryOrItemsUser(Player player, Matchers campaignMatchers) {
        if (campaignMatchers.getHas() != null) {
            Has hasConditions = campaignMatchers.getHas();
            boolean countryMatch = hasConditions.getCountry() == null || hasConditions.getCountry().contains(player.getCountry());
            boolean itemsMatch = hasConditions.getItems() == null || playerInventoryContainsItems(player, hasConditions.getItems());
            return !(countryMatch && itemsMatch);
        }
        return false;
    }

    private boolean matchNotHaveItemsUser(Player player, Matchers campaignMatchers) {
        DoesNotHave doesNotHaveConditions = campaignMatchers.getDoesNotHave();
        return doesNotHaveConditions != null && playerInventoryContainsItems(player, doesNotHaveConditions.getItems());
    }

    private boolean playerInventoryContainsItems(Player player, List<String> itemsToCheck) {
        Inventory inventory = player.getInventory();
        return inventory != null && inventory.getItems() != null &&
                itemsToCheck.stream().anyMatch(item -> inventory.getItems().containsKey(item));
    }

    public Campaign getCurrentCampaign() {
        Campaign campaign = new Campaign();
        campaign.setGame("mygame");
        campaign.setName("mycampaign");
        campaign.setPriority(10.5);
        campaign.setStartDate("2022-01-25 00:00:00Z");
        campaign.setEndDate("2022-02-25 00:00:00Z");
        campaign.setEnabled(true);
        campaign.setLastUpdated("2021-07-13 11:46:58Z");
        Range levelRange = new Range();
        levelRange.setMin(1);
        levelRange.setMax(3);
        Has has = new Has();
        has.setCountry(Arrays.asList("US", "RO", "CA"));
        has.setItems(Arrays.asList("item_1"));
        DoesNotHave doesNotHave = new DoesNotHave();
        doesNotHave.setItems(Arrays.asList("item_4"));
        Matchers matchers = new Matchers();
        matchers.setLevel(levelRange);
        matchers.setHas(has);
        matchers.setDoesNotHave(doesNotHave);
        campaign.setMatcher(matchers);
        return campaign;
    }
}
