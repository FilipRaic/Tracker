package hr.tvz.trackerplatform.wellbeing_tip.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.wellbeing_tip.dto.WellbeingTipDTO;
import hr.tvz.trackerplatform.wellbeing_tip.repository.WellbeingTipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WellbeingTipServiceImpl implements WellbeingTipService {

    private final Mapper mapper;
    private final WellbeingTipRepository wellbeingTipRepository;
    private final DailyCheckRepository dailyCheckRepository;
    private final UserRepository userRepository;


    @Override
    public List<WellbeingTipDTO> findByCategoryAndScore(List<DailyQuestion> listQuestions) {
        if (listQuestions == null || listQuestions.isEmpty()) {
            return new ArrayList<>();
        }

        return listQuestions.stream()
                .map(dailyQuestion -> wellbeingTipRepository.findByCategoryAndScore(dailyQuestion.getCategory(), dailyQuestion.getScore()))
                .filter(Optional::isPresent)
                .map(tipOptional -> mapper.map(tipOptional.get(), WellbeingTipDTO.class))
                .toList();
    }

    public Integer calculateStreak(Long userId) {
        Integer streak = 0;
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).
                    orElseThrow(() -> new TrackerException(ErrorMessage.USER_NOT_FOUND));

            List<DailyCheck> dailyCheckList = dailyCheckRepository.findAllByUser(user);
            for (DailyCheck dailyCheck : dailyCheckList) {
                if (dailyCheck.isCompleted()) {
                    streak++;
                } else if (dailyCheck.getCheckInDate().equals(LocalDate.now())) {
                    // No need to do anything
                } else {
                    break;
                }
            }
        }

        return streak;
    }
}
