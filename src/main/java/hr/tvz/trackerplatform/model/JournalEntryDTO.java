package hr.tvz.trackerplatform.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class JournalEntryDTO {
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
