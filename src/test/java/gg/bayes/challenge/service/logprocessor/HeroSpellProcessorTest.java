package gg.bayes.challenge.service.logprocessor;

import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.logprocessor.HeroSpellProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HeroSpellProcessorTest {

    @InjectMocks
    private HeroSpellProcessor castProcessor;

    @Test
    public void should_process_spell_cast() {

        // Given
        String line1 = "[00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire";
        String line2 = "[00:10:45.930] npc_dota_hero_bloodseeker casts ability bloodseeker_bloodrage (lvl 1) on npc_dota_hero_bloodseeker";

        // When
        castProcessor.parse(line1, new Match(1L));
        castProcessor.parse(line2, new Match(2L));
        List<HeroSpell> list = castProcessor.getHeroSpellList();

        // Then
        assertEquals(2, list.size());

        assertEquals(1, list.get(0).getMatch().getId());
        assertEquals("rubick", list.get(0).getHero());
        assertEquals("rubick_spell_steal", list.get(0).getSpell());
        assertEquals(24*60*1000 + 26*1000 + 996, list.get(0).getTimestamp());

        assertEquals(2, list.get(1).getMatch().getId());
        assertEquals("bloodseeker", list.get(1).getHero());
        assertEquals("bloodseeker_bloodrage", list.get(1).getSpell());
        assertEquals(10*60*1000 + 45*1000 + 930, list.get(1).getTimestamp());
    }

    @Test
    public void should_not_process_spell_cast() {

        // Given
        String line = "[00:37:22.283] npc_dota_hero_mars buys item item_ogre_axe";

        // When
        castProcessor.parse(line, new Match(1L));
        List<HeroSpell> list = castProcessor.getHeroSpellList();

        // Then
        assertEquals(0, list.size());

    }
}
