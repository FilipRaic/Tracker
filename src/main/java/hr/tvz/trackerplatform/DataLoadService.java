package hr.tvz.trackerplatform;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.question.model.Question;
import hr.tvz.trackerplatform.question.repository.QuestionRepository;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
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
    private final WellbeingTipRepository wellbeingTipRepository;
    private final AchievementRepository achievementRepository;


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

        if (wellbeingTipRepository.count() == 0) {
            generateWellbeingTipData();
            log.info("Added sample wellbeing tips");
        }

        if (achievementRepository.count() == 0) {
            generateAchievementData();
            log.info("Added sample achievements");
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

    private void generateWellbeingTipData() {
        wellbeingTipRepository.saveAll(List.of(
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(1)
                        .tipText("Try grounding techniques like naming 5 things you can see, hear, or feel to reduce overwhelming thoughts.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(2)
                        .tipText("Limit information overload by setting boundaries on news and social media consumption.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(3)
                        .tipText("Set simple goals and organize tasks with a planner or to-do list to improve clarity and focus.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(4)
                        .tipText("Engage in creative thinking or hobby projects to strengthen cognitive flexibility.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.MENTAL)
                        .score(5)
                        .tipText("Maintain mental sharpness by learning a new skill, language, or subject youre curious about.")
                        .build(),

                WellbeingTip.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .score(1)
                        .tipText("It’s okay to ask for help—speak to someone you trust or reach out to a mental health professional.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .score(2)
                        .tipText("Allow yourself to feel without suppressing emotions. Writing about your feelings can bring relief.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .score(3)
                        .tipText("Practice self-compassion: talk to yourself as you would to a close friend going through the same situation.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .score(4)
                        .tipText("Continue building emotional intelligence by noticing patterns in your responses and reactions.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.EMOTIONAL)
                        .score(5)
                        .tipText("Celebrate your emotional growth and use it to support others or deepen meaningful relationships.")
                        .build(),

                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(1)
                        .tipText("Focus on rest and gentle movement—short walks, stretching, or light yoga can help restore balance.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(2)
                        .tipText("Drink more water and try to eat one balanced meal a day to slowly rebuild your physical foundation.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(3)
                        .tipText("Add 20–30 minutes of consistent movement to your routine, such as walking, swimming, or cycling.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(4)
                        .tipText("Balance exercise, nutrition, and sleep to keep your body energized and resilient.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.PHYSICAL)
                        .score(5)
                        .tipText("Explore optimizing physical health with strength training, sleep tracking, and performance-based nutrition.")
                        .build(),

                WellbeingTip.builder()
                        .category(QuestionCategory.SOCIAL)
                        .score(1)
                        .tipText("Send a message to someone you miss. Even small contact helps reduce isolation.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.SOCIAL)
                        .score(2)
                        .tipText("Make an effort to connect with one person in a day—this could be a friend, neighbor, or co-worker.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.SOCIAL)
                        .score(3)
                        .tipText("Build deeper connections by opening up about how you’re doing and actively listening in return.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.SOCIAL)
                        .score(4)
                        .tipText("Plan regular social activities that bring you joy and help you recharge through connection.")
                        .build(),
                WellbeingTip.builder()
                        .category(QuestionCategory.SOCIAL)
                        .score(5)
                        .tipText("Strengthen your community by mentoring others, volunteering, or organizing group events.")
                        .build()
        ));
    }

    private void generateAchievementData() {
        achievementRepository.saveAll(List.of(
                Achievement.builder()
                        .name("Welcome")
                        .unlockCondition("")
                        .emoji("😀")
                        .description("Welcome to our mental health tracker app")
                        .build(),
                Achievement.builder()
                        .name("First page")
                        .unlockCondition("Write an entry in your journal")
                        .emoji("📃")
                        .description("You wrote your first entry in your gratitude journal")
                        .build()
        ));
    }
}
