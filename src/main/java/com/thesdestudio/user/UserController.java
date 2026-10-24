package com.thesdestudio.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final CurrentUserService currentUserService;

    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public UserProfileResponse getCurrentUser(@AuthenticationPrincipal OidcUser oidcUser) {
        return UserProfileResponse.from(
                currentUserService.getCurrentUser(oidcUser.getSubject())
        );
    }
}
