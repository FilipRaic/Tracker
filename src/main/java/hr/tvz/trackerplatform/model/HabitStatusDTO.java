package hr.tvz.trackerplatform.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class HabitStatusDTO extends HabitDTO{
    @NotNull
    private boolean done;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    private LocalDate dueDate;
}
