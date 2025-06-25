package hr.tvz.trackerplatform;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class TrackerApplicationTest {

    private final TrackerApplication trackerApplication = new TrackerApplication();

    @Test
    void main_shouldInvokeSpringApplicationRun_withoutException() {
        String[] args = {"--spring.profiles.active=test"};
        try (MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            springApplicationMock.when(() -> SpringApplication.run(TrackerApplication.class, args))
                    .thenReturn(mock(ConfigurableApplicationContext.class));

            TrackerApplication.main(args);

            springApplicationMock.verify(() -> SpringApplication.run(TrackerApplication.class, args));
        }
    }

    @Test
    void modelMapper_shouldReturnNonNullInstance() {
        ModelMapper modelMapper = trackerApplication.modelMapper();

        assertThat(modelMapper)
                .isNotNull()
                .isInstanceOf(ModelMapper.class);
    }

    @Test
    void modelMapper_shouldMapSimpleObjectsCorrectly() {
        ModelMapper modelMapper = trackerApplication.modelMapper();

        Source source = new Source("Test", 123);
        Destination destination = modelMapper.map(source, Destination.class);

        assertThat(destination).isNotNull();
        assertThat(destination.getName()).isEqualTo("Test");
        assertThat(destination.getValue()).isEqualTo(123);
    }

    static class Source {
        private final String name;
        private final int value;

        public Source(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    static class Destination {
        private String name;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
