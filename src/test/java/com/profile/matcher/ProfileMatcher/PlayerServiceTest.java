package com.profile.matcher.ProfileMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.matcher.ProfileMatcher.model.campaign.*;
import com.profile.matcher.ProfileMatcher.model.player.Player;
import com.profile.matcher.ProfileMatcher.repository.PlayerRepository;
import com.profile.matcher.ProfileMatcher.service.PlayerService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player setupTest(String jsonFileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream(jsonFileName);
        return objectMapper.readValue(inputStream, Player.class);
    }

    private void mockPlayerRepository(Player user) {
        when(playerRepository.findByPlayerId(user.getPlayerId())).thenReturn(Optional.of(user));
    }

    private void assertPlayerCampaign(Player user, Campaign mockCampaign, boolean expectedResult) {
        mockPlayerRepository(user);
        Optional<Player> result = playerService.getPlayerByPlayerId(user.getPlayerId());
        assertTrue(result.isPresent());
        Player updatedPlayer = result.get();
        assertEquals(expectedResult, updatedPlayer.getActiveCampaigns().contains(mockCampaign.getName()));
    }

    @ParameterizedTest
    @CsvSource({
            "/test_user_positive.json,true",
            "/test_user_wrong_level.json,false",
            "/test_user_wrong_has_item.json,false",
            "/test_user_wrong_has_not_item.json,false",
            "/test_user_wrong_country.json,false"
    })
    public void testGetPlayerByPlayerId(String jsonFileName, boolean expectedResult) throws IOException {
        Player user = setupTest(jsonFileName);
        Campaign mockCampaign = playerService.getCurrentCampaign();
        assertPlayerCampaign(user, mockCampaign, expectedResult);
    }
}
