package gg.bayes.challenge.service.impl.logprocessor;

import gg.bayes.challenge.entity.HeroBuyItem;
import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.HeroBuyItemRepository;
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
@Getter
@Component
@RequiredArgsConstructor
public class HeroBuyItemProcessor implements LogProcessor {

    @Getter
    private final List<HeroBuyItem> heroBuyItemList = new ArrayList<>();
    private static final Pattern pattern = Pattern.compile("\\[(.*)]\\snpc_dota_hero_(.*)\\sbuys\\sitem\\sitem_(.*)");
    private static final int TIME_GROUP = 1;
    private static final int HERO_GROUP = 2;
    private static final int ITEM_GROUP = 3;

    private final HeroBuyItemRepository repository;

    @Override
    public void parse(String line, Match match) {
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()) {
            HeroBuyItem heroBuyItem = new HeroBuyItem();
            heroBuyItem.setMatch(match);
            heroBuyItem.setHero(matcher.group(HERO_GROUP));
            heroBuyItem.setItem(matcher.group(ITEM_GROUP));
            heroBuyItem.setTimestamp(LocalTime.parse(matcher.group(TIME_GROUP)).get(ChronoField.MILLI_OF_DAY));
            heroBuyItemList.add(heroBuyItem);
        }
    }

    @Override
    public void save() {
        repository.saveAll(heroBuyItemList);
        heroBuyItemList.clear();
    }
}
