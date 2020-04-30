package gg.bayes.challenge.service;

import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.service.impl.KillProcessor;
import gg.bayes.challenge.service.impl.MatchServiceImpl;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    public void shoud_ingest_match() {
        // Given
        BDDMockito.when(
                matchRepository.save(CoreMatchers.any(Match.class))
        ).thenReturn(new Match(1L));
    }
}
