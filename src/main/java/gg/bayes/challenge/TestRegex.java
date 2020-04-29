package gg.bayes.challenge;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
    public static void main(String[] args) {
        String line = "[01:10:01.126] npc_dota_hero_abyssal_underlord is killed by npc_dota_hero_bloodseeker";
        Pattern killPattern = Pattern.compile("\\[(.*)]\\s+npc_dota_hero_(.*)\\s+is\\s+killed\\s+by\\s+npc_dota_hero_(.*)");
        Matcher matcher = killPattern.matcher(line);
        if(matcher.find()) {
            LocalTime time = LocalTime.parse(matcher.group(1));
            System.out.println(time.get(ChronoField.MILLI_OF_DAY));

            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
    }
}
