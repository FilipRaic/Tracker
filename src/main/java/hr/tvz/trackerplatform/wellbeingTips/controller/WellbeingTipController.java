package hr.tvz.trackerplatform.wellbeingTips.controller;

import hr.tvz.trackerplatform.wellbeingTips.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeingTips.service.WellbeingTipService;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.service.DailyQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tip")
public class WellbeingTipController {
    private final DailyQuestionService dailyQuestionService;
    private final WellbeingTipService  wellbeingTipService;

    @GetMapping
    public ResponseEntity<List<WellbeingTipDTO>> findWellbeingTips(){
        List<DailyQuestion> dailyQuestions=dailyQuestionService.findTop4ByOrderByIdDesc();
        List<WellbeingTipDTO> wellbeingTipDTOS =wellbeingTipService.findByCategoryAndAndScore(dailyQuestions);
        return ResponseEntity.ok(wellbeingTipDTOS);
    }

}
