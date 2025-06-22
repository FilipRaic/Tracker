package hr.tvz.trackerplatform.user.security;

import hr.tvz.trackerplatform.shared.exception.TrackerException;
import hr.tvz.trackerplatform.user.model.User;
import hr.tvz.trackerplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static hr.tvz.trackerplatform.shared.exception.ErrorMessage.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    public boolean isCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            userRepository.findById(user.getId())
                    .orElseThrow(() -> new TrackerException(USER_NOT_FOUND));

            return true;
        } else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            String email = userDetails.getUsername();
            return userRepository.existsByEmail(email);
        }

        return false;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return user.getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            String email = userDetails.getUsername();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new TrackerException(USER_NOT_FOUND));

            return user.getId();
        }

        throw new TrackerException(USER_NOT_FOUND);
    }

    public User getCurrentUser() {
        Long currentUserId = getCurrentUserId();
        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new TrackerException(USER_NOT_FOUND));
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            return userDetails.getUsername();
        }

        throw new TrackerException(USER_NOT_FOUND);
    }
}
