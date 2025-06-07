package hr.tvz.trackerplatform.daily_check.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DailyCheckSubmitDTO {

    @NotNull(message = "Daily check must contain ID")
    private Long id;

    @Builder.Default
    @NotNull(message = "Daily check must contain questions")
    @NotEmpty(message = "Daily check must contain questions")
    private List<DailyQuestionDTO> questions = new ArrayList<>();
}
