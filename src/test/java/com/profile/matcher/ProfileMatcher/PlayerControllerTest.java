package com.profile.matcher.ProfileMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.matcher.ProfileMatcher.model.player.Player;
import com.profile.matcher.ProfileMatcher.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerService playerService;

    private Player testPlayer;

    @BeforeEach
    void setUp() throws Exception {
        testPlayer = setupTest("/test_user_positive.json");
    }

    private Player setupTest(String jsonFileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream(jsonFileName);
        return objectMapper.readValue(inputStream, Player.class);
    }

    @Test
    public void testSavePlayer() throws Exception {
        Player player = setupTest("/test_user_positive.json");

        mockMvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(player)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlayerByPlayerId_PlayerFound() throws Exception {
        when(playerService.getPlayerByPlayerId(anyString())).thenReturn(Optional.of(testPlayer));

        mockMvc.perform(get("/players/97983be2-98b7-11e7-90cf-082e5f28d839"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.player_id").value("97983be2-98b7-11e7-90cf-082e5f28d839"))
                .andExpect(jsonPath("$.level").value(3))
                .andExpect(jsonPath("$.xp").value(1000))
                .andExpect(jsonPath("$.country").value("CA"))
                .andExpect(jsonPath("$.devices[0].model").value("apple iphone 11"))
                .andExpect(jsonPath("$.devices[1].model").value("apple iphone 12"))
                .andExpect(jsonPath("$.clan.name").value("hello world clan"))
                .andExpect(jsonPath("$.inventory.items.item_1").value(1))
                .andExpect(jsonPath("$.inventory.items.item_39").value(3))
                .andExpect(jsonPath("$.inventory.items.item_55").value(2));
    }

    @Test
    public void testGetPlayerByPlayerId_PlayerNotFound() throws Exception {
        when(playerService.getPlayerByPlayerId(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/players/97983be2-98b7-11e7-90cf-082e5f28d838"))
                .andExpect(status().isNotFound());
    }
}
