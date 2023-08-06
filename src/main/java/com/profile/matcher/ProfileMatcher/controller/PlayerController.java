package com.profile.matcher.ProfileMatcher.controller;

import com.profile.matcher.ProfileMatcher.model.player.Player;
import com.profile.matcher.ProfileMatcher.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<Player> save(@RequestBody @Valid Player playerDto) {
        return ResponseEntity.ok(playerService.save(playerDto));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayerByPlayerId(@PathVariable String playerId) {
        Optional<Player> playerOptional = playerService.getPlayerByPlayerId(playerId);
        if (playerOptional.isPresent()) {
            return ResponseEntity.ok(playerOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
