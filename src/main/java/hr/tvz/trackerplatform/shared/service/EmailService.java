package hr.tvz.trackerplatform.shared.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.habit.model.Habit;
import hr.tvz.trackerplatform.user.model.User;

public interface EmailService {

    void sendDailyCheckEmail(DailyCheck dailyCheck, User user);

    void sendStreakWarningEmail(Habit habit);

    void sendResetPasswordEmail(User user, String token);
}
