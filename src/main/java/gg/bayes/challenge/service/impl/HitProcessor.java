package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.HeroDamage;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroDamageRepository;
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
public class HitProcessor extends LogProcessor {
    static final Pattern hitPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+hits\\s+npc_dota_hero_(.*)\\s+with\\s+(.*)\\s+for\\s+(.*)\\s+damage");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int HERO_TARGET_GROUP = 3;
    private static final int HIT_BY_GROUP = 4;
    private static final int DAMAGE_GROUP = 5;

    private final HeroDamageRepository heroDamageRepository;

    @Override
    public void process(String line, Match match) {
        Matcher matcher = hitPattern.matcher(line);
        if(matcher.find()) {
            HeroDamage hd = new HeroDamage();
            hd.setMatch(match);
            hd.setHero(matcher.group(HERO_GROUP));
            hd.setTarget(matcher.group(HERO_TARGET_GROUP));
            hd.setDamage(Integer.valueOf(matcher.group(DAMAGE_GROUP)));
            hd.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroDamageRepository.save(hd);
        }
        this.next.process(line, match);
    }
}
