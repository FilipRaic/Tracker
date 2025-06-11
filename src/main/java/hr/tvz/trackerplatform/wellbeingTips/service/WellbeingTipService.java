package hr.tvz.trackerplatform.wellbeingTips.service;

import hr.tvz.trackerplatform.wellbeingTips.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;

import java.util.List;

public interface WellbeingTipService {
    List<WellbeingTipDTO> findByCategoryAndAndScore(List<DailyQuestion> listQuestions);
}
