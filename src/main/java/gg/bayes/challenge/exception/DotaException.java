package gg.bayes.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class DotaException extends RuntimeException{

    private final String message;

    private final HttpStatus status;
}
