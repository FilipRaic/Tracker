package hr.tvz.trackerplatform.user.security;

import hr.tvz.trackerplatform.shared.exception.ErrorMessage;
import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.enums.Role;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserSecurity userSecurity;

    private User appUser;
    private UserDetails springUserDetails;

    @BeforeEach
    void setUp() {
        appUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.USER)
                .password("encodedPassword")
                .build();

        springUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("john.doe@example.com")
                .password("encodedPassword")
                .roles(Role.USER.name())
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void isCurrentUser_shouldReturnTrue_withAppUserPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

        boolean result = userSecurity.isCurrentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findById(appUser.getId());
        assertThat(result).isTrue();
    }

    @Test
    void isCurrentUser_shouldReturnTrue_withSpringUserDetailsPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);
        when(userRepository.existsByEmail(springUserDetails.getUsername())).thenReturn(true);

        boolean result = userSecurity.isCurrentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).existsByEmail(springUserDetails.getUsername());
        assertThat(result).isTrue();
    }

    @Test
    void isCurrentUser_shouldReturnFalse_whenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        boolean result = userSecurity.isCurrentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verifyNoInteractions(userRepository);
        assertThat(result).isFalse();
    }

    @Test
    void isCurrentUser_shouldReturnFalse_whenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        boolean result = userSecurity.isCurrentUser();

        verify(securityContext).getAuthentication();
        verifyNoInteractions(userRepository);
        assertThat(result).isFalse();
    }

    @Test
    void isCurrentUser_shouldThrowException_whenAppUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userRepository.findById(appUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userSecurity.isCurrentUser())
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findById(appUser.getId());
    }

    @Test
    void isCurrentUser_shouldReturnFalse_withInvalidPrincipalType() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("invalid-principal");

        boolean result = userSecurity.isCurrentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(userRepository);
        assertThat(result).isFalse();
    }

    @Test
    void getCurrentUserId_shouldReturnId_withAppUserPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(appUser);

        Long result = userSecurity.getCurrentUserId();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(userRepository);
        assertThat(result).isEqualTo(appUser.getId());
    }

    @Test
    void getCurrentUserId_shouldReturnId_withSpringUserDetailsPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);
        when(userRepository.findByEmail(springUserDetails.getUsername())).thenReturn(Optional.of(appUser));

        Long result = userSecurity.getCurrentUserId();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findByEmail(springUserDetails.getUsername());
        assertThat(result).isEqualTo(appUser.getId());
    }

    @Test
    void getCurrentUserId_shouldReturnNull_whenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Long result = userSecurity.getCurrentUserId();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verifyNoInteractions(userRepository);
        assertThat(result).isNull();
    }

    @Test
    void getCurrentUserId_shouldReturnNull_whenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Long result = userSecurity.getCurrentUserId();

        verify(securityContext).getAuthentication();
        verifyNoInteractions(userRepository);
        assertThat(result).isNull();
    }

    @Test
    void getCurrentUserId_shouldThrowException_whenUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);
        when(userRepository.findByEmail(springUserDetails.getUsername())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userSecurity.getCurrentUserId())
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findByEmail(springUserDetails.getUsername());
    }

    @Test
    void getCurrentUserId_shouldThrowException_withInvalidPrincipalType() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("invalid-principal");

        assertThatThrownBy(() -> userSecurity.getCurrentUserId())
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(userRepository);
    }

    @Test
    void getCurrentUser_shouldReturnUser_withAppUserPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(appUser);
        when(userRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

        User result = userSecurity.getCurrentUser();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findById(appUser.getId());
        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void getCurrentUser_shouldReturnUser_withSpringUserDetailsPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);
        when(userRepository.findByEmail(springUserDetails.getUsername())).thenReturn(Optional.of(appUser));
        when(userRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

        User result = userSecurity.getCurrentUser();

        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void getCurrentUser_shouldThrowException_whenUserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);
        when(userRepository.findByEmail(springUserDetails.getUsername())).thenReturn(Optional.of(appUser));
        when(userRepository.findById(appUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userSecurity.getCurrentUser())
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verify(userRepository).findByEmail(springUserDetails.getUsername());
        verify(userRepository).findById(appUser.getId());
    }

    @Test
    void getCurrentUserEmail_shouldReturnEmail_withSpringUserDetailsPrincipal() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(springUserDetails);

        String result = userSecurity.getCurrentUserEmail();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(userRepository);
        assertThat(result).isEqualTo(springUserDetails.getUsername());
    }

    @Test
    void getCurrentUserEmail_shouldReturnNull_whenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String result = userSecurity.getCurrentUserEmail();

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verifyNoInteractions(userRepository);
        assertThat(result).isNull();
    }

    @Test
    void getCurrentUserEmail_shouldReturnNull_whenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = userSecurity.getCurrentUserEmail();

        verify(securityContext).getAuthentication();
        verifyNoInteractions(userRepository);
        assertThat(result).isNull();
    }

    @Test
    void getCurrentUserEmail_shouldThrowException_withInvalidPrincipalType() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("invalid-principal");

        assertThatThrownBy(() -> userSecurity.getCurrentUserEmail())
                .isInstanceOf(TrackerException.class)
                .hasMessage(ErrorMessage.USER_NOT_FOUND.getMessage());

        verify(securityContext).getAuthentication();
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(userRepository);
    }
}
