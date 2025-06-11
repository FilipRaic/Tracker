package hr.tvz.trackerplatform.wellbeingTips.service;

import hr.tvz.trackerplatform.wellbeingTips.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeingTips.model.WellbeingTip;
import hr.tvz.trackerplatform.wellbeingTips.repository.WellbeingTipRepository;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WellbeingTipServiceImpl implements WellbeingTipService{

    private final WellbeingTipRepository wellbeingTipRepository;
    @Override
    public List<WellbeingTipDTO> findByCategoryAndAndScore(List<DailyQuestion> listQuestions) {
        List<WellbeingTipDTO> allTips = listQuestions.stream()
                .map(dailyQuestion -> wellbeingTipRepository.findByCategoryAndAndScore(dailyQuestion.getCategory(), dailyQuestion.getScore()))
                .map(tip -> new WellbeingTipDTO(tip.getCategory(), tip.getScore(), tip.getTipText()))
                .collect(Collectors.toList());
        return allTips;
    }

    private WellbeingTipDTO mapToWellbeingTipDTO(WellbeingTip wellbeingTip) {
        return WellbeingTipDTO.builder()
                .tipText(wellbeingTip.getTipText())
                .score(wellbeingTip.getScore())
                .category(wellbeingTip.getCategory())
                .build();
    }

    private WellbeingTip mapToWellbeingTip(WellbeingTipDTO wellbeingTipDTO) {
        return WellbeingTip.builder()
                .tipText(wellbeingTipDTO.getTipText())
                .score(wellbeingTipDTO.getScore())
                .category(wellbeingTipDTO.getCategory())
                .build();
    }
}
