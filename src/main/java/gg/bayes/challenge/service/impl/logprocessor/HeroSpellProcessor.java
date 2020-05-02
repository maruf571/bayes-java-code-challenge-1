package gg.bayes.challenge.service.impl.logprocessor;

import gg.bayes.challenge.entity.HeroSpell;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroSpellRepository;
import gg.bayes.challenge.service.LogProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeroSpellProcessor implements LogProcessor {

    @Getter
    private final List<HeroSpell> heroSpellList = new ArrayList<>();
    protected static final Pattern pattern = Pattern.compile("\\[(.*)]\\snpc_dota_hero_(.*)\\scasts\\sability\\s(.*)\\s\\(.*\\)\\son\\s(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int SPELL_GROUP = 3;

    private final HeroSpellRepository repository;

    @Override
    public void parse(String line, Match match) {
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            HeroSpell heroSpell = new HeroSpell();
            heroSpell.setMatch(match);
            heroSpell.setHero(matcher.group(HERO_GROUP));
            heroSpell.setSpell(matcher.group(SPELL_GROUP));
            heroSpell.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroSpellList.add(heroSpell);
        }
    }

    @Override
    public void save() {
        repository.saveAll(heroSpellList);
        heroSpellList.clear();
    }


}
