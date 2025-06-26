package hr.tvz.trackerplatform.daily_check.service;

import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DailyQuestionServiceTest {

    @Mock
    private DailyCheckRepository dailyCheckRepository;

    @InjectMocks
    private DailyQuestionService dailyQuestionService;

    @Test
    void findTop4ByOrderByIdDesc_returnsTop4Questions() {
        Long userId = 1L;
        List<DailyQuestion> allQuestions = List.of(
                createQuestion(4L),
                createQuestion(3L),
                createQuestion(2L),
                createQuestion(1L)
        );

        when(dailyCheckRepository.findAllQuestionsByUserId(userId)).thenReturn(allQuestions);

        // Act
        List<DailyQuestion> actual = dailyQuestionService.findTop4ByOrderByIdDesc(userId);

        // Assert
        assertThat(actual).hasSize(4);
        assertThat(actual)
                .extracting(DailyQuestion::getId)
                .containsExactly( 4L, 3L, 2L, 1L);
    }

    private DailyQuestion createQuestion(Long id) {
        return DailyQuestion.builder()
                .id(id)
                .category(QuestionCategory.MENTAL)
                .content("Content EN")
                .contentDe("Content DE")
                .contentHr("Content HR")
                .score(3)
                .build();
    }
}
