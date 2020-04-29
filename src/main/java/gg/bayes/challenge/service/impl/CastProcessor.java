package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroSpellRepository;
import gg.bayes.challenge.service.LogProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CastProcessor extends LogProcessor {
    static final Pattern castPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+casts\\s+ability\\s+(.*)\\s\\(.*\\)\\s+on\\s+(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int SPELL_GROUP = 3;

    private final HeroSpellRepository heroSpellRepository;

    @Override
    public void process(String line, Match match) {
        Matcher matcher = castPattern.matcher(line);
        if(matcher.find()) {
            HeroSpell spell = new HeroSpell();
            spell.setMatch(match);
            spell.setHero(matcher.group(HERO_GROUP));
            spell.setSpell(matcher.group(SPELL_GROUP));
            spell.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroSpellRepository.save(spell);
        }
        this.next.process(line, match);
    }
}
