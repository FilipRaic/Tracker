package hr.tvz.trackerplatform.wellbeing_tip.service;

import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;

import java.util.List;

public interface WellbeingTipService {
    List<WellbeingTipDTO> findByCategoryAndScore(List<DailyQuestion> listQuestions);
    Integer calculateStreak(Long userId);
}
