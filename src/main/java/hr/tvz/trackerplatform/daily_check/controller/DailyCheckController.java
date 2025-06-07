package hr.tvz.trackerplatform.daily_check.controller;

import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckSubmitDTO;
import hr.tvz.trackerplatform.daily_check.service.DailyCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/daily-check")
public class DailyCheckController {

    private final DailyCheckService dailyCheckService;

    @GetMapping("/{uuid}")
    public ResponseEntity<DailyCheckDTO> getCheckInByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(dailyCheckService.getDailyCheckByUuid(uuid));
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitDailyCheck(@Valid @RequestBody DailyCheckSubmitDTO dailyCheckSubmitDTO) {
        log.info("Submitting responses for daily check {}", dailyCheckSubmitDTO.getId());

        dailyCheckService.submitDailyCheck(dailyCheckSubmitDTO);

        return ResponseEntity.ok().build();
    }
}
