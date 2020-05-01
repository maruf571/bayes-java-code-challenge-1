package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.Match;

public interface LogProcessorManager {

    void parse(String line, Match match);

    void save();
}
