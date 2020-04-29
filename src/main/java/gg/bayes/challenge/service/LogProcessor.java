package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.Match;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class LogProcessor {
    private Match match;
    protected LogProcessor next;
    public LogProcessor setNext(LogProcessor next) {
        this.next = next;
        return next;
    }
    public abstract void process(String line, Match match);
}
