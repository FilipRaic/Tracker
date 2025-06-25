package hr.tvz.trackerplatform.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private WebAuthenticationDetailsSource webAuthenticationDetailsSource;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("john.doe@example.com")
                .password("encodedPassword")
                .roles("USER")
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternal_shouldAuthenticate_whenValidTokenAndNoAuthentication() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String email = "john.doe@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
        verify(jwtService).extractUsername(token);
        verify(securityContext).getAuthentication();
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(token, userDetails);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenNoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService, securityContext);
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenInvalidAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService, securityContext);
    }

    @Test
    void doFilterInternal_shouldAuthenticateForRefreshTokenEndpoint() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String email = "john.doe@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/auth/refresh-token");
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(securityContext).setAuthentication(any(Authentication.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenTokenInvalid() throws ServletException, IOException {
        String token = "invalid-jwt-token";
        String email = "john.doe@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
        verify(jwtService).extractUsername(token);
        verify(securityContext).getAuthentication();
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(token, userDetails);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(webAuthenticationDetailsSource);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenEmailIsNull() throws ServletException, IOException {
        String token = "jwt-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractUsername(token)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
        verify(jwtService).extractUsername(token);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userDetailsService, securityContext, webAuthenticationDetailsSource);
    }

    @Test
    void doFilterInternal_shouldContinueFilterChain_whenAuthenticationExists() throws ServletException, IOException {
        String token = "jwt-token";
        String email = "john.doe@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(request).getRequestURI();
        verify(jwtService).extractUsername(token);
        verify(securityContext).getAuthentication();
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userDetailsService, webAuthenticationDetailsSource);
        verify(securityContext, never()).setAuthentication(any());
    }
}
