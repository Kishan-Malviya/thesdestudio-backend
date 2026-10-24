package com.thesdestudio.user;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String email,
        String displayName,
        String profileImageUrl,
        UserRole role
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getProfileImageUrl(),
                user.getRole()
        );
    }
}
