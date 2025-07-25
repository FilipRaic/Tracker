package hr.tvz.trackerplatform.daily_check.service;

import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyQuestionService {

    private final DailyQuestionRepository dailyQuestionRepository;
    private final DailyCheckRepository dailyCheckRepository;

    public List<DailyQuestion> findTop4ByOrderByIdDesc(Long userId) {
        return dailyCheckRepository.findAllQuestionsByUserId(userId);
    }
}
