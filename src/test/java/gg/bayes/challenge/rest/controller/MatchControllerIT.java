package gg.bayes.challenge.rest.controller;


import gg.bayes.challenge.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import javax.transaction.Transactional;


@SpringBootTest
@ActiveProfiles("it")
@AutoConfigureMockMvc
public class MatchControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatchService matchService;

    @Test
    @Transactional
    public void should_parse_logfile() throws Exception {

        // Given
        String payload =
                "[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown\n" +
                "[00:08:46.693] npc_dota_hero_snapfire buys item item_clarity\n" +
                "[00:08:48.159] npc_dota_hero_puck buys item item_tango\n" +
                "[00:08:49.758] npc_dota_hero_puck buys item item_sobi_mask\n"
                ;

        // When and Then
        this.mockMvc.perform(post(MatchController.URL)
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isNumber())
        ;
    }

    @Test
    @Transactional
    public void should_get_match() throws Exception {

        // Given
        String payload = "[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars\n" +
                "[00:35:30.536] npc_dota_hero_puck is killed by npc_dota_hero_mars\n" +
                "[00:30:47.103] npc_dota_hero_puck is killed by npc_dota_hero_mars\n" +
                "[00:30:59.667] npc_dota_hero_bane is killed by npc_dota_hero_snapfire\n" +
                "[00:36:00.672] npc_dota_hero_bane is killed by npc_dota_hero_snapfire\n" +
                "[00:36:46.762] npc_dota_hero_mars is killed by npc_dota_hero_abyssal_underlord\n" +
                "";

        Long matchId = matchService.ingestMatch(payload);

        // When and Then
        // api response sort by hero name
        this.mockMvc.perform(get(MatchController.URL + "/" +matchId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].hero").value("abyssal_underlord"))
                .andExpect(jsonPath("$[0].kills").value("1"))
                .andExpect(jsonPath("$[1].hero").value("mars"))
                .andExpect(jsonPath("$[1].kills").value("3"))
                .andExpect(jsonPath("$[2].hero").value("snapfire"))
                .andExpect(jsonPath("$[2].kills").value("2"))

        ;
    }

    @Test
    @Transactional
    public void should_get_buy_item() throws Exception {

        // Given
        String payload =
                "[00:08:48.159] npc_dota_hero_puck buys item item_tango\n"+
                "[00:08:49.758] npc_dota_hero_puck buys item item_sobi_mask\n"+
                "[00:08:49.792] npc_dota_hero_puck buys item item_ring_of_basilius\n"+
                "[00:08:49.792] npc_dota_hero_puck buys item item_recipe_ring_of_basilius\n"+
                "[00:08:50.292] npc_dota_hero_mars buys item item_quelling_blade\n"+
                "[00:08:50.325] npc_dota_hero_puck buys item item_branches\n"+
                "[00:08:50.458] npc_dota_hero_puck buys item item_branches\n"+
                "[00:08:50.891] npc_dota_hero_rubick buys item item_sobi_mask\n"+
                "[00:08:50.925] npc_dota_hero_rubick buys item item_ring_of_basilius\n"+
                "[00:08:50.925] npc_dota_hero_rubick buys item item_recipe_ring_of_basilius\n"+
                "[00:08:51.391] npc_dota_hero_rubick buys item item_branches\n"+
                "";

        Long matchId = matchService.ingestMatch(payload);

        // When and Then
        // api response sort by timestamp
        this.mockMvc.perform(get(MatchController.URL + "/" + matchId +"/puck/items" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].item").value("tango"))
                .andExpect(jsonPath("$[0].timestamp").value(8*60*1000 + 48*1000 +159))
                .andExpect(jsonPath("$[1].item").value("sobi_mask"))
                .andExpect(jsonPath("$[1].timestamp").value(8*60*1000 + 49*1000 + 758))
        ;

        this.mockMvc.perform(get(MatchController.URL + "/" + matchId +"/mars/items" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].item").value("quelling_blade"))
                .andExpect(jsonPath("$[0].timestamp").value(8*60*1000 + 50*1000 + 292))
        ;
    }


    @Test
    @Transactional
    public void should_get_spell() throws Exception {

        // Given
        String payload =
                        "[00:10:41.998] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 1) on dota_unknown\n" +
                        "[00:17:26.366] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 3) on dota_unknown\n" +
                        "[00:10:52.129] npc_dota_hero_rubick casts ability rubick_fade_bolt (lvl 1) on npc_dota_creep_goodguys_ranged\n"+
                        "[00:10:55.928] npc_dota_hero_bane casts ability bane_brain_sap (lvl 1) on npc_dota_hero_puck\n"+
                        "";

        Long matchId = matchService.ingestMatch(payload);

        // When and Then
        // api response sort by timestamp
        this.mockMvc.perform(get(MatchController.URL + "/" + matchId +"/abyssal_underlord/spells" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].spell").value("abyssal_underlord_firestorm"))
                .andExpect(jsonPath("$[0].casts").value(2))
        ;

        this.mockMvc.perform(get(MatchController.URL + "/" + matchId +"/rubick/spells" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].spell").value("rubick_fade_bolt"))
                .andExpect(jsonPath("$[0].casts").value(1))
        ;
    }

    @Test
    @Transactional
    public void should_get_damage() throws Exception {

        // Given
        String payload =
                        "[00:17:26.399] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bane with abyssal_underlord_firestorm for 48 damage (794->746)\n" +
                        "[00:17:26.399] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bane with abyssal_underlord_firestorm for 21 damage (746->725)\n" +
                        "[00:17:26.399] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 48 damage (454->406)\n" +
                        "[00:17:26.399] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 25 damage (405->380)\n" +
                        "[00:17:26.965] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with dota_unknown for 66 damage (379->313)\n" ;

        Long matchId = matchService.ingestMatch(payload);

        // When and Then
        // api response sort by timestamp
        this.mockMvc.perform(get(MatchController.URL + "/" + matchId +"/abyssal_underlord/damage" )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(payload)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].target").value("bane"))
                .andExpect(jsonPath("$[0].damage_instances").value(2))
                .andExpect(jsonPath("$[0].total_damage").value(69))

                .andExpect(jsonPath("$[1].target").value("bloodseeker"))
                .andExpect(jsonPath("$[1].damage_instances").value(3))
                .andExpect(jsonPath("$[1].total_damage").value(139))
        ;

    }
}
