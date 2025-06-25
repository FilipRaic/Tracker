package hr.tvz.trackerplatform.journal_entry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryDTO {

    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
