package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.rest.model.HeroDamages;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(MatchController.URL)
@RequiredArgsConstructor
public class MatchController {
    static final String URL = "/api/match";
    private final MatchService matchService;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        final Long matchId = matchService.ingestMatch(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchId);
    }

    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
        return ResponseEntity.ok(matchService.getMatch(matchId));
    }

    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
                                                    @PathVariable("heroName") String heroName) {
        return ResponseEntity.ok(matchService.getHeroItems(matchId, heroName));
    }

    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
        return ResponseEntity.ok(matchService.getHeroSpells(matchId, heroName));
    }

    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamages>> getDamage(@PathVariable("matchId") Long matchId,
                                                       @PathVariable("heroName") String heroName) {
        return ResponseEntity.ok(matchService.getHeroDamage(matchId, heroName));
    }
}
