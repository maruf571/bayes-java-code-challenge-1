package gg.bayes.challenge.service.logprocessor;

import gg.bayes.challenge.entity.HeroBuyItem;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.logprocessor.HeroBuyItemProcessor;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HeroBuyItemProcessorTest {

    @InjectMocks
    private HeroBuyItemProcessor heroBuyItemProcessor;

    @Test
    public void should_process_item_buy() {

        // Given
        String line1 = "[00:37:22.283] npc_dota_hero_mars buys item item_ogre_axe";
        String line2 = "[00:09:01.189] npc_dota_hero_bloodseeker buys item item_quelling_blade";

        // When
        heroBuyItemProcessor.parse(line1, new Match(123L));
        heroBuyItemProcessor.parse(line2, new Match(123L));
        List<HeroBuyItem> list = heroBuyItemProcessor.getHeroBuyItemList();

        // Then
        assertEquals(2, list.size());
        assertEquals("mars", list.get(0).getHero());
        assertEquals("ogre_axe", list.get(0).getItem());
        assertEquals(37*60*1000 + 22*1000 + 283, list.get(0).getTimestamp());

        assertEquals("bloodseeker", list.get(1).getHero());
        assertEquals("quelling_blade", list.get(1).getItem());
        assertEquals(9*60*1000 + 1*1000 + 189, list.get(1).getTimestamp());
    }

    @Test
    public void should_not_process_item_buy() {

        // Given
        String line = "[00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire";

        // When
        heroBuyItemProcessor.parse(line, new Match(1L));
        List<HeroBuyItem> list = heroBuyItemProcessor.getHeroBuyItemList();

        // Then
        assertEquals(0, list.size());
    }
}
