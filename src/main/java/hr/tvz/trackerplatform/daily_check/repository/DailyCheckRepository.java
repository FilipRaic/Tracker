package hr.tvz.trackerplatform.daily_check.repository;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyCheckRepository extends JpaRepository<DailyCheck, Long> {
    List<DailyCheck> findAllByUser(User user);

    Optional<DailyCheck> findByUuid(UUID uuid);

    List<DailyCheck> findAllByUserAndCompletedTrue(User user);

    boolean existsByCheckInDateAndUser(LocalDate checkInDate, User user);

    @Query("SELECT q FROM DailyCheck dc JOIN dc.questions q WHERE dc.user.id = :userId")
    List<DailyQuestion> findAllQuestionsByUserId(@Param("userId") Long userId);
}
