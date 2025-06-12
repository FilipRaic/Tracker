package hr.tvz.trackerplatform.shared.service;

import hr.tvz.trackerplatform.daily_check.model.DailyCheck;
import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static User user;

    @Mock
    private MimeMessage mimeMessage;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    private final String fromEmail = "test@example.com";
    private final String frontendUrl = "http://localhost:3000";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", fromEmail);
        ReflectionTestUtils.setField(emailService, "frontendUrl", frontendUrl);

        user = User.builder()
                .id(1L)
                .firstName("Davis")
                .lastName("Davis")
                .email("davis@mail.com")
                .role(Role.USER)
                .build();
    }

    @Test
    void sendDailyCheckEmail_shouldSendEmail() throws MessagingException {
        UUID uuid = UUID.randomUUID();
        DailyCheck dailyCheck = DailyCheck.builder()
                .id(1L)
                .uuid(uuid)
                .checkInDate(LocalDate.now())
                .build();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("daily-check-email"), any(Context.class))).thenReturn("<html>Email content</html>");

        emailService.sendDailyCheckEmail(dailyCheck, user);

        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq("daily-check-email"), any(Context.class));
        verify(mailSender).send(mimeMessage);

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("daily-check-email"), contextCaptor.capture());

        Context capturedContext = contextCaptor.getValue();
        assertThat(capturedContext.getVariable("firstName")).isEqualTo(user.getFirstName());
        assertThat(capturedContext.getVariable("dailyCheckUrl")).isEqualTo(frontendUrl + "/daily-check/" + uuid);
    }

    @Test
    void sendDailyCheckEmail_shouldHandleMessagingException() {
        UUID uuid = UUID.randomUUID();
        DailyCheck dailyCheck = DailyCheck.builder()
                .id(1L)
                .uuid(uuid)
                .checkInDate(LocalDate.now())
                .build();

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("daily-check-email"), any(Context.class))).thenReturn("<html>Email content</html>");
        doThrow(new RuntimeException("Test exception")).when(mailSender).send(mimeMessage);

        assertThatThrownBy(() -> emailService.sendDailyCheckEmail(dailyCheck, user))
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.ERROR_GENERATING_EMAIL.getMessage());

        verify(mailSender).createMimeMessage();
        verify(templateEngine).process(eq("daily-check-email"), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }
}
