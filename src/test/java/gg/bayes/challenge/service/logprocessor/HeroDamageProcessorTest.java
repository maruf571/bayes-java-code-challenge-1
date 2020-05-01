package gg.bayes.challenge.service.logprocessor;

import gg.bayes.challenge.entity.HeroDamage;
import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.logparser.HeroDamageProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HeroDamageProcessorTest {

    @InjectMocks
    private HeroDamageProcessor damageProcessor;

    @Test
    public void should_process_spell_cast() {

        // Given
        String line = "[00:25:05.520] npc_dota_hero_pangolier hits npc_dota_hero_death_prophet with pangolier_gyroshell for 148 damage (1380->1232)";

        // When
        damageProcessor.parse(line, new Match(123L));
        List<HeroDamage> list = damageProcessor.getHeroDamageList();

        // Then
        assertEquals("pangolier", list.get(0).getHero());
        assertEquals("death_prophet", list.get(0).getTarget());
        assertEquals(148, list.get(0).getDamage());
        assertEquals(25*60*1000 + 5*1000 + 520, list.get(0).getTimestamp());
    }

    @Test
    public void should_not_process_spell_cast() {

        // Given
        String line = "[00:37:22.283] npc_dota_hero_mars buys item item_ogre_axe";

        // When
        damageProcessor.parse(line, new Match(123L));
        List<HeroDamage> list = damageProcessor.getHeroDamageList();

        // Then
        assertEquals(0, list.size());
    }
}
