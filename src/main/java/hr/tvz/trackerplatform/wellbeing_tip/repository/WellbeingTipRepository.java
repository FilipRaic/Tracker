package hr.tvz.trackerplatform.wellbeing_tip.repository;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WellbeingTipRepository extends JpaRepository<WellbeingTip, Long> {
    Optional<WellbeingTip> findByCategoryAndScore(QuestionCategory category, Integer score);
}
