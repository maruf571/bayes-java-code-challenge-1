package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.Match;

public interface LogProcessor {

    void parse(String line, Match match);

    void save();
}
