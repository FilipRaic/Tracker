package hr.tvz.trackerplatform.shared.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MockMailServiceImpl implements EmailService {

    @Override
    public void sendDailyCheckEmail(DailyCheck dailyCheck, User user) {
        log.info("Sending MOCK daily check email to user with ID: {}", user.getId());
    }

    @Override
    public void sendStreakWarningEmail(Habit habit) {
        log.info("Sending MOCK streak warning email to user with ID: {}", habit.getUser().getId());
    }

    @Override
    public void sendResetPasswordEmail(User user, String token) {
        log.info("Sending MOCK reset password email to user with ID: {}", user.getId());
    }
}
