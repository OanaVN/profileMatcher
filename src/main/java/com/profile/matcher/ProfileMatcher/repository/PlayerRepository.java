package com.profile.matcher.ProfileMatcher.repository;

import com.profile.matcher.ProfileMatcher.model.player.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByPlayerId(String playerId);
}