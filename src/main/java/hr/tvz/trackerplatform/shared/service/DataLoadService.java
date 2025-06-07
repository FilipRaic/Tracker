package hr.tvz.trackerplatform.shared.service;

import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.question.model.Question;
import hr.tvz.trackerplatform.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DataLoadService implements ApplicationRunner {

    private final HabitFrequencyRepository habitFrequencyRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (habitFrequencyRepository.count() == 0) {
            generateHabitFrequencyData();
            log.info("Added all habit frequencies");
        }

        if (questionRepository.count() == 0) {
            generateDailyQuestionData();
            log.info("Added sample well-being questions");
        }
    }

    private void generateHabitFrequencyData() {
        habitFrequencyRepository.saveAll(List.of(
                HabitFrequency.builder()
                        .name("day")
                        .build(),
                HabitFrequency.builder()
                        .name("week")
                        .build(),
                HabitFrequency.builder()
                        .name("month")
                        .build(),
                HabitFrequency.builder()
                        .name("year")
                        .build()
        ));
    }

    private void generateDailyQuestionData() {
        questionRepository.saveAll(List.of(
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How focused do you feel today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How well are you able to concentrate on tasks?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How clear is your thinking today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How creative do you feel today?")
                        .build(),

                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How happy do you feel today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How well are you managing stress today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How optimistic do you feel about the future?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How emotionally stable do you feel today?")
                        .build(),

                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How energetic do you feel today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How well did you sleep last night?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How physically active have you been today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How satisfied are you with your eating habits today?")
                        .build(),

                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How connected do you feel to others today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How satisfied are you with your social interactions today?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How supported do you feel by others?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How well are you communicating with others today?")
                        .build()
        ));
    }
}
