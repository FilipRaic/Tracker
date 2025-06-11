package hr.tvz.trackerplatform.daily_check.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DailyCheckDTO {

    private Long id;

    @Builder.Default
    private List<DailyQuestionDTO> questions = new ArrayList<>();

    private boolean completed;

    private String userFirstName;
}
