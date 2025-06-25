package hr.tvz.trackerplatform.daily_check.service;

import hr.tvz.trackerplatform.daily_check.dto.DailyCheckDTO;
import hr.tvz.trackerplatform.daily_check.dto.DailyCheckSubmitDTO;
import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.daily_check.model.DailyQuestion;
import hr.tvz.trackerplatform.daily_check.repository.DailyCheckRepository;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.shared.mapper.Mapper;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DailyCheckService {

    private final Mapper mapper;
    private final UserSecurity userSecurity;
    private final DailyCheckRepository dailyCheckRepository;

    public List<DailyCheckDTO> findAllCompletedCheckIns() {
        User currentUser = userSecurity.getCurrentUser();

        return mapper.mapList(dailyCheckRepository.findAllByUserAndCompletedTrue(currentUser), DailyCheckDTO.class);
    }

    @Transactional(readOnly = true)
    public DailyCheckDTO getDailyCheckByUuid(UUID uuid) {
        DailyCheck dailyCheck = dailyCheckRepository.findByUuid(uuid)
                .orElseThrow(() -> new TrackerException(ErrorMessage.DAILY_CHECK_NOT_FOUND));

        if (dailyCheck.isCompleted()) {
            throw new TrackerException(ErrorMessage.DAILY_CHECK_ALREADY_SUBMITTED);
        }

        return mapper.map(dailyCheck, DailyCheckDTO.class);
    }

    @Transactional
    public void submitDailyCheck(DailyCheckSubmitDTO dailyCheckSubmitDTO) {
        DailyCheck dailyCheck = dailyCheckRepository.findById(dailyCheckSubmitDTO.getId())
                .orElseThrow(() -> new TrackerException(ErrorMessage.DAILY_CHECK_NOT_FOUND));

        List<DailyQuestion> questions = mapper.mapList(dailyCheckSubmitDTO.getQuestions(), DailyQuestion.class);
        dailyCheck.submitResponses(questions);

        dailyCheckRepository.save(dailyCheck);
    }
}
