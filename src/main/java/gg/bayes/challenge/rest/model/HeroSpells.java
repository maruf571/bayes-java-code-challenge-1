package gg.bayes.challenge.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@AllArgsConstructor
public class HeroSpells {

    private String spell;

    private Long casts;
}
