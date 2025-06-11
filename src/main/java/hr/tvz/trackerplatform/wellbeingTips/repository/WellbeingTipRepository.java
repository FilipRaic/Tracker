package hr.tvz.trackerplatform.wellbeingTips.repository;

import hr.tvz.trackerplatform.wellbeingTips.model.WellbeingTip;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WellbeingTipRepository extends JpaRepository<WellbeingTip,Long> {
    WellbeingTip findByCategoryAndAndScore(QuestionCategory category, Integer score);
}
