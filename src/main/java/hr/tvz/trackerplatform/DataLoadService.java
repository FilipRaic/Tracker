package hr.tvz.trackerplatform;

import hr.tvz.trackerplatform.achievement.model.Achievement;
import hr.tvz.trackerplatform.achievement.model.UserAchievement;
import hr.tvz.trackerplatform.achievement.repository.AchievementRepository;
import hr.tvz.trackerplatform.achievement.repository.UserAchievementRepository;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.habit.model.HabitCompletion;
import hr.tvz.trackerplatform.habit.model.HabitFrequency;
import hr.tvz.trackerplatform.habit.repository.HabitCompletionRepository;
import hr.tvz.trackerplatform.habit.repository.HabitFrequencyRepository;
import hr.tvz.trackerplatform.habit.repository.HabitRepository;
import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.journal_entry.repository.JournalEntryRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import hr.tvz.trackerplatform.question.model.Question;
import hr.tvz.trackerplatform.question.repository.QuestionRepository;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.wellbeing_tip.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DataLoadService implements ApplicationRunner {

    private final Random random = new Random();

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionRepository questionRepository;
    private final DailyCheckRepository dailyCheckRepository;
    private final AchievementRepository achievementRepository;
    private final WellbeingTipRepository wellbeingTipRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final HabitFrequencyRepository habitFrequencyRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Override
    @Transactional
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

        if (userRepository.count() == 0) {
            generateUser();
            log.info("Added sample user");
        }

        if (journalEntryRepository.count() == 0) {
            generateJournalEntryData();
            log.info("Added sample journal entries");
        }

        if (dailyCheckRepository.count() == 0) {
            generateDailyCheckData();
            log.info("Added sample daily checks");
        }

        if (habitRepository.count() == 0) {
            generateHabitData();
            log.info("Added sample habits");
        }

        if (userAchievementRepository.count() == 0) {
            generateUserAchievementData();
            log.info("Added sample user achievements");
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
                        .tipText("Set simple goals and organize(tasks with a planner or to-do list to improve clarity and focus.")
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

    private void generateUser() {
        userRepository.save(User.builder()
                .email("test@mail.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.USER)
                .password(passwordEncoder.encode("Test123!"))
                .build());
    }

    private void generateJournalEntryData() {
        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        journalEntryRepository.saveAll(List.of(
                JournalEntry.builder()
                        .date(LocalDate.now().minusDays(2))
                        .description("Today was a good day")
                        .user(user)
                        .build(),
                JournalEntry.builder()
                        .date(LocalDate.now().minusDays(1))
                        .description("I had a bad day")
                        .user(user)
                        .build(),
                JournalEntry.builder()
                        .date(LocalDate.now())
                        .description("Today was a great day")
                        .user(user)
                        .build()
        ));
    }

    private void generateDailyCheckData() {
        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        QuestionCategory[] categories = {
                QuestionCategory.MENTAL,
                QuestionCategory.PHYSICAL,
                QuestionCategory.EMOTIONAL,
                QuestionCategory.SOCIAL
        };

        for (int day = 3; day >= 1; day--) {
            List<DailyQuestion> dailyQuestions = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                DailyQuestion question = DailyQuestion.builder()
                        .category(categories[i])
                        .content(getQuestionContentEn(i))
                        .contentDe(getQuestionContentDe(i))
                        .contentHr(getQuestionContentHr(i))
                        .score(generateRandomScore())
                        .build();
                dailyQuestions.add(question);
            }

            List<DailyQuestion> savedQuestions = dailyQuestionRepository.saveAll(dailyQuestions);

            DailyCheck dailyCheck = DailyCheck.builder()
                    .uuid(UUID.randomUUID())
                    .checkInDate(LocalDate.now().minusDays(day))
                    .completed(true)
                    .completedAt(Instant.now().minus(day, java.time.temporal.ChronoUnit.DAYS).plusSeconds(60))
                    .createdAt(Instant.now().minus(day, java.time.temporal.ChronoUnit.DAYS))
                    .user(user)
                    .questions(savedQuestions)
                    .build();

            dailyCheckRepository.saveAll(List.of(dailyCheck));
        }
    }

    private String getQuestionContentEn(int index) {
        String[] questions = {
                "How focused do you feel today?",
                "Heow much energy do you have today?",
                "How would you rate your mood today?",
                "How connected do you feel with others today?"
        };
        return questions[index];
    }

    private String getQuestionContentDe(int index) {
        String[] questions = {
                "Wie konzentriert fühlen Sie sich heute?",
                "Wie viel Energie haben Sie heute?",
                "Wie würden Sie Ihre Stimmung heute bewerten?",
                "Wie verbunden fühlen Sie sich heute mit anderen?"
        };
        return questions[index];
    }

    private String getQuestionContentHr(int index) {
        String[] questions = {
                "Koliko se danas osjećaš fokusirano?",
                "Koliko energije imaš danas?",
                "Kako bi ocijenio svoje raspoloženje danas?",
                "Koliko se osjećaš povezano s drugima danas?"
        };
        return questions[index];
    }

    private int generateRandomScore() {
        return random.nextInt(5) + 1;
    }

    private void generateHabitData() {
        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        HabitFrequency daily = habitFrequencyRepository.findByName("day").orElseThrow();
        HabitFrequency weekly = habitFrequencyRepository.findByName("week").orElseThrow();
        HabitFrequency monthly = habitFrequencyRepository.findByName("month").orElseThrow();

        List<Habit> habits = List.of(
                Habit.builder()
                        .name("Drink 2L of water")
                        .begin(LocalDate.now().minusDays(3))
                        .description("Stay hydrated by drinking at least 2 liters of water daily")
                        .habitFrequency(daily)
                        .user(user)
                        .build(),
                Habit.builder()
                        .name("Weekly workout")
                        .begin(LocalDate.now().minusDays(7))
                        .description("Complete a full-body workout once a week")
                        .habitFrequency(weekly)
                        .user(user)
                        .build(),
                Habit.builder()
                        .name("Monthly meditation")
                        .begin(LocalDate.now().minusDays(30))
                        .description("Practice a 30-minute meditation session monthly")
                        .habitFrequency(monthly)
                        .user(user)
                        .build()
        );

        List<Habit> savedHabits = habitRepository.saveAll(habits);

        List<HabitCompletion> completions = new ArrayList<>();
        for (int day = 2; day >= 0; day--) {
            LocalDate completionDate = LocalDate.now().minusDays(day);

            completions.add(HabitCompletion.builder()
                    .completionDate(completionDate)
                    .done(day != 1)
                    .habit(savedHabits.get(0))
                    .build());

            if (day == 2) {
                completions.add(HabitCompletion.builder()
                        .completionDate(completionDate)
                        .done(true)
                        .habit(savedHabits.get(1))
                        .build());
            }

            if (day == 0) {
                completions.add(HabitCompletion.builder()
                        .completionDate(completionDate)
                        .done(true)
                        .habit(savedHabits.get(2))
                        .build());
            }
        }

        habitCompletionRepository.saveAll(completions);
    }

    private void generateUserAchievementData() {
        User user = userRepository.findAll().stream().findFirst().orElseThrow();

        Achievement welcome = achievementRepository.findByName("Welcome").orElseThrow();
        Achievement firstPage = achievementRepository.findByName("First page").orElseThrow();

        userAchievementRepository.saveAll(List.of(
                UserAchievement.builder()
                        .user(user)
                        .achievement(welcome)
                        .completed(true)
                        .build(),
                UserAchievement.builder()
                        .user(user)
                        .achievement(firstPage)
                        .completed(true)
                        .build()
        ));
    }
}
