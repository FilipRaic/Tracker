package hr.tvz.trackerplatform.wellbeing_tip.controller;

import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.service.DailyQuestionService;
import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeing_tip.service.WellbeingTipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tip")
public class WellbeingTipController {

    private final DailyQuestionService dailyQuestionService;
    private final WellbeingTipService wellbeingTipService;

    @GetMapping
    public ResponseEntity<List<WellbeingTipDTO>> findWellbeingTips() {
        List<DailyQuestion> dailyQuestions = dailyQuestionService.findTop4ByOrderByIdDesc();
        List<WellbeingTipDTO> wellbeingTipDTOS = wellbeingTipService.findByCategoryAndScore(dailyQuestions);

        return ResponseEntity.ok(wellbeingTipDTOS);
    }
}
