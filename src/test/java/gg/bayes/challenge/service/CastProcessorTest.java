package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.BaseSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.CastProcessor;
import gg.bayes.challenge.service.impl.EndProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CastProcessorTest {

    @InjectMocks
    private CastProcessor castProcessor;

    @Test
    public void should_process_spell_cast() {

        // Given
        String line = "[00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire";

        // When
        boolean res = castProcessor.process(line, new Match(1L));
        List<BaseSpell> list = castProcessor.getHeroSpells();

        // Then
        assertTrue(res);
        assertEquals(1, list.get(0).getMatch().getId());
        assertEquals(1, list.size());
        assertEquals("rubick", list.get(0).getHero());
        assertEquals("rubick_spell_steal", list.get(0).getSpell());
        assertEquals(24*60*1000 + 26*1000 + 996, list.get(0).getTimestamp());
    }

    @Test
    public void should_not_process_spell_cast() {

        // Given
        String line = "[00:37:22.283] npc_dota_hero_mars buys item item_ogre_axe";

        // When
        castProcessor.next = new EndProcessor();
        boolean res = castProcessor.process(line, new Match(1L));
        List<BaseSpell> list = castProcessor.getHeroSpells();


        // Then
        assertFalse(res);
        assertEquals(0, list.size());

    }
}
