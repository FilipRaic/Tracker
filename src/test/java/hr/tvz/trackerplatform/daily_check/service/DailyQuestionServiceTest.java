package hr.tvz.trackerplatform.daily_check.service;

import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyQuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyQuestionServiceTest {

    @Mock
    private DailyQuestionRepository dailyQuestionRepository;

    @InjectMocks
    private DailyQuestionService dailyQuestionService;

    @Test
    void findTop4ByOrderByIdDesc_returnsTop4Questions() {
        List<DailyQuestion> expected = List.of(
                DailyQuestion.builder().id(1L).build(),
                DailyQuestion.builder().id(2L).build(),
                DailyQuestion.builder().id(3L).build(),
                DailyQuestion.builder().id(4L).build()
        );

        when(dailyQuestionRepository.findTop4ByOrderByIdDesc()).thenReturn(expected);

        List<DailyQuestion> actual = dailyQuestionService.findTop4ByOrderByIdDesc(1L);

        assertThat(actual).isEqualTo(expected);
    }
}
