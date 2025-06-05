package hr.tvz.trackerplatform.journal_entry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
