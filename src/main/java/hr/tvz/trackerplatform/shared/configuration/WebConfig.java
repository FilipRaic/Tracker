package hr.tvz.trackerplatform.shared.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // No view controllers needed as we're using Angular for the frontend
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Empty implementation
    }
}
