package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.HeroItem;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroItemRepository;
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
public class BuyProcessor extends LogProcessor {

    static final Pattern buyPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+buys\\s+item\\s+item_(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int ITEM_GROUP = 3;

    private final HeroItemRepository heroItemRepository;

    @Override
    public void process(String line, Match match) {
        Matcher matcher = buyPattern.matcher(line);
        if(matcher.find()) {
            HeroItem heroItem = new HeroItem();
            heroItem.setMatch(match);
            heroItem.setTimestamp((long) LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroItem.setHero(matcher.group(HERO_GROUP));
            heroItem.setItem(matcher.group(ITEM_GROUP));
            heroItemRepository.save(heroItem);
        }
        this.next.process(line, match);
    }
}
