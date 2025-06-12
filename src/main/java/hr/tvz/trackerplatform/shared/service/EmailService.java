package hr.tvz.trackerplatform.shared.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendDailyCheckEmail(DailyCheck dailyCheck, User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Your Daily Well-Being Check-In");

            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("dailyCheckUrl", frontendUrl + "/daily-check/" + dailyCheck.getUuid());

            String emailContent = templateEngine.process("daily-check-email", context);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("Daily check email sent to user with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to send daily check to user with ID: {}", user.getId(), e);
            throw new TrackerException(ErrorMessage.ERROR_GENERATING_EMAIL);
        }
    }
}
