package hr.tvz.trackerplatform.journal_entry.repository;

import hr.tvz.trackerplatform.journal_entry.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    JournalEntry findByDate(LocalDate date);

    void deleteByDate(LocalDate date);
}
