package hr.tvz.trackerplatform.shared.scheduler;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.question.model.Question;
import hr.tvz.trackerplatform.question.repository.QuestionRepository;
import hr.tvz.trackerplatform.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyCheckScheduler {

    private static final int NUMBER_OF_QUESTIONS = 4;

    private final EmailService emailService;
    private final QuestionRepository questionRepository;
    private final DailyCheckRepository dailyCheckRepository;

    @Transactional
    @Scheduled(cron = "${scheduler.cron.daily-check}")
    public void scheduleDailyChecks() {
        log.info("Starting scheduled task to generate and send daily checks");

        generateAndSendDailyChecks();

        log.info("Completed scheduled task to generate and send daily checks");
    }

    private void generateAndSendDailyChecks() {
        log.info("Generating daily check");

        try {
            Optional<DailyCheck> dailyCheck = generateDailyCheck();
            dailyCheck.ifPresent(emailService::sendDailyCheckEmail);
        } catch (Exception e) {
            log.error("Failed to generate or send daily check", e);
        }
    }

    private Optional<DailyCheck> generateDailyCheck() {
        LocalDate today = LocalDate.now();

        if (dailyCheckRepository.existsByCheckInDate(today)) {
            log.info("Daily check already exists for this date {}", today);

            return Optional.empty();
        }

        List<Question> randomQuestions = questionRepository.findRandomActiveQuestions(NUMBER_OF_QUESTIONS);
        if (randomQuestions.size() < NUMBER_OF_QUESTIONS) {
            log.warn("Not enough questions available. Found only {} questions.", randomQuestions.size());
            return Optional.empty();
        }

        DailyCheck dailyCheck = DailyCheck.builder()
                .checkInDate(today)
                .createdAt(LocalDateTime.now())
                .questions(mapToDailyQuestions(randomQuestions))
                .completed(false)
                .build();

        DailyCheck savedDailyCheck = dailyCheckRepository.saveAndFlush(dailyCheck);
        log.info("Generated daily check with UUID {}", savedDailyCheck.getUuid());

        return Optional.of(savedDailyCheck);
    }

    private List<DailyQuestion> mapToDailyQuestions(List<Question> questions) {
        return new ArrayList<>(questions
                .stream()
                .map(question ->
                        DailyQuestion.builder()
                                .category(question.getCategory())
                                .content(question.getContent())
                                .build())
                .toList());
    }
}
