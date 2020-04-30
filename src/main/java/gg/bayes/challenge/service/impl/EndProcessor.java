package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.LogProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EndProcessor extends LogProcessor {

    @Override
    public boolean process(String line, Match match) {
        log.info(line);
        return false;
    }

    @Override
    public void save() {
        // Do nothing
    }
}
