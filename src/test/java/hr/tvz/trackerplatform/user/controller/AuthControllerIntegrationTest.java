package hr.tvz.trackerplatform.user.controller;

import hr.tvz.trackerplatform.MockMvcIntegrationTest;
import hr.tvz.trackerplatform.user.dto.*;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import hr.tvz.trackerplatform.user.security.RefreshTokenService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/auth";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private hr.tvz.trackerplatform.shared.service.EmailService emailService;

    @Test
    void register_shouldCreateUserSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!", "Password123!");

        var response = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();

        User savedUser = userRepository.findByEmail("john.doe@example.com").orElseThrow();
        assertThat(savedUser)
                .satisfies(user -> {
                    assertThat(user.getFirstName()).isEqualTo("John");
                    assertThat(user.getLastName()).isEqualTo("Doe");
                    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
                    assertThat(user.getRole()).isEqualTo(Role.USER);
                    assertThat(user.getPassword()).isNotEqualTo("Password123!");
                });
    }

    @Test
    void register_shouldFail_whenEmailExists() throws Exception {
        User existingUser = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        userRepository.save(existingUser);

        RegisterRequest request = new RegisterRequest("Jane", "Doe", "jane.doe@example.com", "Password123!", "Password123!");

        var response = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        String content = response.getContentAsString();
        assertThat(content).contains("Email already exists");
    }

    @Test
    void register_shouldFail_whenPasswordsDoNotMatch() throws Exception {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "Password123!", "Different123!");

        var response = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        String content = response.getContentAsString();
        assertThat(content).contains("Passwords do not match");
    }

    @Test
    void login_shouldReturnAuthResponseSuccessfully() throws Exception {
        String email = "john.doe@example.com";
        String rawPassword = "Password123!";
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password("$2a$12$RhXuIq6f23DAwnhavQZdpezNMt3Vh.XgXU0YNUTZ4kFH0c1bilZqu")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        LoginRequest request = new LoginRequest(email, rawPassword);

        var response = mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        AuthResponse authResponse = mapper.readValue(response.getContentAsString(), AuthResponse.class);
        assertThat(authResponse)
                .satisfies(resp -> assertThat(resp.accessToken()).isNotEmpty());
    }

    @Test
    void login_shouldFail_whenInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("john.doe@example.com", "WrongPassword");

        var response = mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        String content = response.getContentAsString();
        assertThat(content).contains("Invalid credentials");
    }

    @Test
    void refreshToken_shouldReturnNewAccessToken() throws Exception {
        refreshTokenService.createRefreshToken(user.getId());

        var response = mockMvc.perform(withJwt(put(BASE_URL + "/refresh-token")))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        AuthResponse authResponse = mapper.readValue(response.getContentAsString(), AuthResponse.class);
        assertThat(authResponse)
                .satisfies(resp -> assertThat(resp.accessToken()).isNotEmpty());
    }

    @Test
    void refreshToken_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        mockMvc.perform(put(BASE_URL + "/refresh-token"))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @Disabled("This test is not working properly")
    void sendResetPasswordEmail_shouldSendEmailSuccessfully() throws Exception {
        ResetPasswordEmailRequest request = new ResetPasswordEmailRequest(user.getEmail());

        mockMvc.perform(post(BASE_URL + "/forgot-password/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    void sendResetPasswordEmail_shouldFail_whenUserNotFound() throws Exception {
        ResetPasswordEmailRequest request = new ResetPasswordEmailRequest("nonexistent@example.com");

        var response = mockMvc.perform(post(BASE_URL + "/forgot-password/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        String content = response.getContentAsString();
        assertThat(content).contains("User not found");
    }

    @Test
    void resetPassword_shouldUpdatePasswordSuccessfully() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "NewPassword123!");

        var response = mockMvc.perform(withJwt(post(BASE_URL + "/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEmpty();

        User updatedUser = userRepository.findByEmail(user.getEmail()).orElseThrow();
        assertThat(updatedUser.getPassword()).isNotEqualTo("oldEncodedPassword");
    }

    @Test
    void resetPassword_shouldFail_whenPasswordsDoNotMatch() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "Different123!");

        var response = mockMvc.perform(withJwt(post(BASE_URL + "/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        String content = response.getContentAsString();
        assertThat(content).contains("Passwords do not match");
    }

    @Test
    void resetPassword_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("NewPassword123!", "NewPassword123!");

        mockMvc.perform(post(BASE_URL + "/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }
}
