package gg.bayes.challenge.service;


import gg.bayes.challenge.exception.DotaException;
import gg.bayes.challenge.repository.MatchRepository;
import static org.junit.jupiter.api.Assertions.*;

import gg.bayes.challenge.service.impl.MatchValidationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MatchValidationTest {

    @Mock
    MatchRepository matchRepository;

    @InjectMocks
    MatchValidationImpl matchValidation;

    @Test
    public void should_through_exception_for_match() {
        // Given
        Mockito.when(matchRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(DotaException.class, () -> {
            matchValidation.validateMatch(123L);
        });
    }

    @Test
    public void should_through_exception_for_name() {
        assertThrows(DotaException.class, () -> {
            matchValidation.validateHeroName("    ");
        });
    }
}
