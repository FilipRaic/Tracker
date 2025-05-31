package hr.tvz.trackerplatform.repository;

import hr.tvz.trackerplatform.model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface JournalEntryRepository extends JpaRepository<JournalEntry,Long> {
    JournalEntry findByDate(LocalDate date);
    void deleteByDate(LocalDate date);
}
