package hr.tvz.trackerplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.user.security.JwtService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import test_container.PostgresContainerExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@ExtendWith({SpringExtension.class, PostgresContainerExtension.class})
public abstract class MockMvcIntegrationTest {

    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    protected User user;
    protected MockMvc mockMvc;

    @BeforeEach
    public void init() {
        user = User.builder()
                .firstName("Test")
                .lastName("User")
                .email(UUID.randomUUID() + "@example.com")
                .role(Role.USER)
                .password("password")
                .build();

        user = userRepository.save(user);

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void tearDown() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                List<String> tablesToTruncate = Arrays.asList(
                        "daily_question",
                        "daily_check",
                        "achievement",
                        "journal_entry",
                        "habit_frequency",
                        "habit_completion",
                        "habit",
                        "wellbeing_tip",
                        "refresh_token",
                        "application_user"
                );

                for (String table : tablesToTruncate) {
                    entityManager.createNativeQuery("TRUNCATE TABLE \"%s\" RESTART IDENTITY CASCADE".formatted(table))
                            .executeUpdate();
                }

                entityManager.flush();
            }
        });
    }

    protected MockHttpServletRequestBuilder withJwt(MockHttpServletRequestBuilder requestBuilder) {
        String jwtToken = jwtService.generateToken(user);

        return requestBuilder.header("Authorization", "Bearer " + jwtToken);
    }
}
