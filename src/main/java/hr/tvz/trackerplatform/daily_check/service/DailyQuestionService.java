package hr.tvz.trackerplatform.daily_check.service;

import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyQuestionService {

    private final DailyQuestionRepository dailyQuestionRepository;

    public List<DailyQuestion> findTop4ByOrderByIdDesc(){
        List<DailyQuestion> dailyQuestions=dailyQuestionRepository.findTop4ByOrderByIdDesc();
        return dailyQuestions;
    }
}
