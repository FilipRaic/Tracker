package hr.tvz.trackerplatform.user.repository;

import hr.tvz.trackerplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = """
            SELECT au.*
            FROM application_user au
                LEFT JOIN daily_check dc ON au.id = dc.user_id AND dc.check_in_date = :date
            WHERE dc IS NULL
            """, nativeQuery = true)
    List<User> findAllWithoutDailyCheckForDate(LocalDate date);
}
