package gg.bayes.challenge.service.impl.logparser;

import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.service.LogProcessorManager;
import gg.bayes.challenge.service.LogProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogProcessorManagerImpl implements LogProcessorManager {

    private final List<LogProcessor> logProcessorList = new ArrayList<>();
    private final HeroBuyItemProcessor heroBuyItemProcessor;
    private final HeroKillProcessor heroKillProcessor;
    private final HeroDamageProcessor heroDamageProcessor;
    private final HeroSpellProcessor heroSpellProcessor;

    @PostConstruct
    public void prepareProcessorList() {
        logProcessorList.add(heroBuyItemProcessor);
        logProcessorList.add(heroDamageProcessor);
        logProcessorList.add(heroKillProcessor);
        logProcessorList.add(heroSpellProcessor);
    }

    @Override
    public void parse(String line, Match match) {
        for(LogProcessor parser : logProcessorList) {
            parser.parse(line, match);
        }
    }

    @Override
    public void save() {
        for(LogProcessor lp : logProcessorList) {
            lp.save();
        }
    }
}
