package gg.bayes.challenge.service.logprocessor;

import gg.bayes.challenge.entity.Match;
import gg.bayes.challenge.repository.MatchRepository;
import gg.bayes.challenge.service.impl.*;

import static org.junit.jupiter.api.Assertions.*;

import gg.bayes.challenge.service.impl.logparser.HeroBuyItemProcessor;
import gg.bayes.challenge.service.impl.logparser.HeroDamageProcessor;
import gg.bayes.challenge.service.impl.logparser.HeroKillProcessor;
import gg.bayes.challenge.service.impl.logparser.HeroSpellProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private HeroBuyItemProcessor buyProcessor;

    @Mock
    private HeroSpellProcessor castProcessor;

    @Mock
    private HeroDamageProcessor hitProcessor;

    @Mock
    private HeroKillProcessor heroKillProcessor;

    @InjectMocks
    private MatchServiceImpl matchService;


    @Test
    public void should_ingest_match() {
        // Given
        String payload = "this is dummy payload";
        ReflectionTestUtils.setField(matchService, "logProcessor", buyProcessor);

        // When
        when(matchRepository.save(any(Match.class))).thenReturn(new Match(123L));
        //when(buyProcessor.process(anyString(), any(Match.class))).thenReturn(true);

        // Then
        Long res = matchService.ingestMatch(payload);
        assertEquals(123L, res);
    }

}
