package hr.tvz.trackerplatform;

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
                        .contentDe("Wie konzentriert fühlst du dich heute?")
                        .contentHr("Koliko se danas osjećaš fokusirano?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How well are you able to concentrate on tasks?")
                        .contentDe("Wie gut kannst du dich heute auf Aufgaben konzentrieren?")
                        .contentHr("Koliko se danas možeš usredotočiti na zadatke?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How clear is your thinking today?")
                        .contentDe("Wie klar ist dein Denken heute?")
                        .contentHr("Koliko je tvoje razmišljanje danas jasno?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.MENTAL)
                        .content("How creative do you feel today?")
                        .contentDe("Wie kreativ fühlst du dich heute?")
                        .contentHr("Koliko se danas osjećaš kreativno?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How happy do you feel today?")
                        .contentDe("Wie glücklich fühlst du dich heute?")
                        .contentHr("Koliko se danas osjećaš sretno?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How well are you managing stress today?")
                        .contentDe("Wie gut gehst du heute mit Stress um?")
                        .contentHr("Koliko dobro danas upravljaš stresom?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How optimistic do you feel about the future?")
                        .contentDe("Wie optimistisch bist du in Bezug auf die Zukunft?")
                        .contentHr("Koliko si optimističan/na u vezi budućnosti?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .content("How emotionally stable do you feel today?")
                        .contentDe("Wie emotional stabil fühlst du dich heute?")
                        .contentHr("Koliko se danas osjećaš emocionalno stabilno?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How energetic do you feel today?")
                        .contentDe("Wie energiegeladen fühlst du dich heute?")
                        .contentHr("Koliko energije danas imaš?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How well did you sleep last night?")
                        .contentDe("Wie gut hast du letzte Nacht geschlafen?")
                        .contentHr("Koliko si dobro spavao/la prošle noći?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How physically active have you been today?")
                        .contentDe("Wie körperlich aktiv warst du heute?")
                        .contentHr("Koliko si danas bio/la fizički aktivan/na?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .content("How satisfied are you with your eating habits today?")
                        .contentDe("Wie zufrieden bist du heute mit deinen Essgewohnheiten?")
                        .contentHr("Koliko si danas zadovoljan/na svojim prehrambenim navikama?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How connected do you feel to others today?")
                        .contentDe("Wie verbunden fühlst du dich heute mit anderen?")
                        .contentHr("Koliko se danas osjećaš povezan/a s drugima?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How satisfied are you with your social interactions today?")
                        .contentDe("Wie zufrieden bist du heute mit deinen sozialen Interaktionen?")
                        .contentHr("Koliko si danas zadovoljan/na svojim društvenim interakcijama?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How supported do you feel by others?")
                        .contentDe("Wie sehr fühlst du dich von anderen unterstützt?")
                        .contentHr("Koliko se osjećaš podržano od strane drugih?")
                        .build(),
                Question.builder()
                        .category(QuestionCategory.SOCIAL)
                        .content("How well are you communicating with others today?")
                        .contentDe("Wie gut kommunizierst du heute mit anderen?")
                        .contentHr("Koliko dobro danas komuniciraš s drugima?")
                        .build()
        ));
    }
}
