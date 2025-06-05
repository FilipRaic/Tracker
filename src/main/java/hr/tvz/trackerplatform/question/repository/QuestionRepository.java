package hr.tvz.trackerplatform.question.repository;

import hr.tvz.trackerplatform.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
            SELECT * FROM question ORDER BY RANDOM() LIMIT :count
            """, nativeQuery = true)
    List<Question> findRandomActiveQuestions(int count);
}
