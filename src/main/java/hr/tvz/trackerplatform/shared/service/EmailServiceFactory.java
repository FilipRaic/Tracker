package hr.tvz.trackerplatform.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmailServiceFactory {

    @Value("${spring.mail.use-mock}")
    private boolean useMock;

    private final EmailServiceImpl emailServiceImpl;
    private final MockMailServiceImpl mockMailServiceImpl;

    @Bean
    public EmailService emailService() {
        return useMock ? mockMailServiceImpl : emailServiceImpl;
    }
}
