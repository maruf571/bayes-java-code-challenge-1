package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public abstract class LogProcessor {
    protected LogProcessor next;
    protected List<BaseItem> heroItems = new ArrayList<>();
    protected List<BaseDamage> heroDamages = new ArrayList<>();
    protected List<BaseKill> heroKills = new ArrayList<>();
    protected List<BaseSpell> heroSpells = new ArrayList<>();

    public LogProcessor setNext(LogProcessor next) {
        this.next = next;
        return next;
    }
    public  abstract boolean process(String line, Match match);
    public  abstract void save();
}
