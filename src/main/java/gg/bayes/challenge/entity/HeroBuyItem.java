package gg.bayes.challenge.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class HeroBuyItem implements HeroActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Match match;

    private String hero;

    private String item;

    private Long timestamp;
}
