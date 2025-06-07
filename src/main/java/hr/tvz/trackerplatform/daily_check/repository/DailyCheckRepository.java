package hr.tvz.trackerplatform.daily_check.repository;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyCheckRepository extends JpaRepository<DailyCheck, Long> {

    Optional<DailyCheck> findByUuid(UUID uuid);

    List<DailyCheck> findByCheckInDate(LocalDate checkInDate);

    boolean existsByCheckInDate(LocalDate checkInDate);
}
