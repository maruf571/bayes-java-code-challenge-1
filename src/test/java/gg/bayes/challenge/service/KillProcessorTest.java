package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.BaseKill;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.EndProcessor;
import gg.bayes.challenge.service.impl.KillProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KillProcessorTest {

    @InjectMocks
    private KillProcessor killProcessor;

    @Test
    public void should_process_kill() {

        // Given
        String line = "[00:25:21.316] npc_dota_hero_puck is killed by npc_dota_hero_bane";

        // When
        boolean res = killProcessor.process(line, new Match(4L));
        List<BaseKill> list = killProcessor.getHeroKills();

        // Then
        assertTrue(res);
        assertEquals(1, list.size());
        assertEquals(4, list.get(0).getMatch().getId());
        assertEquals("bane", list.get(0).getHero());
        assertEquals("puck", list.get(0).getTarget());;
        assertEquals(25*60*1000 + 21*1000 + 316, list.get(0).getTimestamp());
    }

    @Test
    public void should_not_process_kill() {

        // Given
        String line = "[00:25:21.116] npc_dota_hero_death_prophet hits npc_dota_hero_puck with dota_unknown for 121 damage (138->17)";

        // When
        killProcessor.next = new EndProcessor();
        boolean res = killProcessor.process(line, new Match(4L));
        List<BaseKill> list = killProcessor.getHeroKills();

        // Then
        assertFalse(res);
        assertEquals(0, list.size());
    }
}
