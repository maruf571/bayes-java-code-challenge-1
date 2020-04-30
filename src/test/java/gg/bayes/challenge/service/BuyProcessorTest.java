package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.BaseItem;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.impl.BuyProcessor;
import static org.junit.jupiter.api.Assertions.*;

import gg.bayes.challenge.service.impl.EndProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BuyProcessorTest {

    @InjectMocks
    private BuyProcessor buyProcessor;

    @Test
    public void should_process_item_buy() {

        // Given
        String line = "[00:37:22.283] npc_dota_hero_mars buys item item_ogre_axe";

        // When
         boolean res = buyProcessor.process(line, new Match(1L));
        List<BaseItem> list = buyProcessor.getHeroItems();

        // Then
        assertTrue(res);
        assertEquals(1, list.get(0).getMatch().getId());
        assertEquals(1, list.size());
        assertEquals("mars", list.get(0).getHero());
        assertEquals("ogre_axe", list.get(0).getItem());
        assertEquals(37*60*1000 + 22*1000 + 283, list.get(0).getTimestamp());
    }

    @Test
    public void should_not_process_item_buy() {

        // Given
        String line = "[00:24:26.996] npc_dota_hero_rubick casts ability rubick_spell_steal (lvl 1) on npc_dota_hero_snapfire";

        // When
        buyProcessor.next = new EndProcessor();
        boolean res = buyProcessor.process(line, new Match(1L));
        List<BaseItem> list = buyProcessor.getHeroItems();

        // Then
        assertFalse(res);
        assertEquals(0, list.size());
    }
}
