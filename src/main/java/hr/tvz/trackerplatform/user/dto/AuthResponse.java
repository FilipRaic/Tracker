package hr.tvz.trackerplatform.user.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken) {
}
