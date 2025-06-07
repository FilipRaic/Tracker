package hr.tvz.trackerplatform.daily_check.dto;

import hr.tvz.trackerplatform.question.enums.QuestionCategory;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DailyQuestionDTO {

    private Long id;
    private QuestionCategory category;
    private String content;
    private Integer score;
}
