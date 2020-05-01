package gg.bayes.challenge.repository;

import gg.bayes.challenge.entity.HeroBuyItem;
import gg.bayes.challenge.rest.model.HeroItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeroBuyItemRepository extends JpaRepository<HeroBuyItem, Long> {
    @Query("select new gg.bayes.challenge.rest.model.HeroItems(item.item, item.timestamp) " +
            "FROM HeroBuyItem item " +
            "where item.match.id = ?1 and item.hero = ?2 order by item.timestamp")
    List<HeroItems> heroItem(Long matchId, String heroName);
}
