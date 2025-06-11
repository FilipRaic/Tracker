package hr.tvz.trackerplatform.daily_check.repository;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyQuestionRepository extends JpaRepository<DailyQuestion, Long> {
    List<DailyQuestion> findTop4ByOrderByIdDesc();
}
