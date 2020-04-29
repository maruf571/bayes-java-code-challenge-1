package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.LogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UsesProcessor extends LogProcessor {
    static final Pattern usesPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+uses\\s+item_(.*)");
    @Override
    public void process(String line, Match match) {
        Matcher matcher = usesPattern.matcher(line);
        if(matcher.find()) {
            // Do nothing. You are not important right now
        }
    }
}
