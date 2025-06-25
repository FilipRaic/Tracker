package hr.tvz.trackerplatform.journal_entry.repository;

import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import hr.tvz.trackerplatform.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findAllByUser(User user);

    Optional<JournalEntry> findByUserAndDate(User user, LocalDate date);

    void deleteById(Long id);
}
