package hr.tvz.trackerplatform;

import hr.tvz.trackerplatform.model.HabitFrequency;
import hr.tvz.trackerplatform.repository.HabitFrequencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Profile({"local", "dev"})
@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {
    private final HabitFrequencyRepository habitFrequencyRepository;

    @Override
    public void run(ApplicationArguments args) {
        if(habitFrequencyRepository.count() == 0) {
            generateHabitFrequencyData();
            log.info("Added all habit frequencies");
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
}


