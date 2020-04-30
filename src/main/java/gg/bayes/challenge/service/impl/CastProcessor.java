package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.BaseSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroSpellRepository;
import gg.bayes.challenge.service.LogProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CastProcessor extends LogProcessor {
    static final Pattern castPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+casts\\s+ability\\s+(.*)\\s\\(.*\\)\\s+on\\s+(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int SPELL_GROUP = 3;

    private final HeroSpellRepository spellRepository;

    @Override
    public boolean process(String line, Match match) {
        Matcher matcher = castPattern.matcher(line);
        if(matcher.find()) {
            BaseSpell spell = new BaseSpell();
            spell.setMatch(match);
            spell.setHero(matcher.group(HERO_GROUP));
            spell.setSpell(matcher.group(SPELL_GROUP));
            spell.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroSpells.add(spell);
            return true;
        }
        return this.next.process(line, match);
    }

    @Override
    public void save() {
        spellRepository.saveAll(heroSpells);
        heroSpells.clear();
        this.next.save();
    }
}
