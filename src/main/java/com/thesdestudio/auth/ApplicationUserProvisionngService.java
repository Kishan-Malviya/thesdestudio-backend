package com.thesdestudio.auth;

import com.thesdestudio.user.User;
import com.thesdestudio.user.UserRepository;
import com.thesdestudio.user.UserRole;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserProvisionngService {
    private final UserRepository userRepository;

    public ApplicationUserProvisionngService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User provisionUser(OidcUser oidcUser) {
        String googleSubject = oidcUser.getSubject();
        String email = oidcUser.getEmail();

        if (email == null || email.isBlank() || !Boolean.TRUE.equals(oidcUser.getEmailVerified())) {
            throw authenticationError("A verified Google email address is required.");
        }

        User user = userRepository
                .findByGoogleSubject(googleSubject)
                .orElse(null);

        if (user == null) {
            if (userRepository.existsByEmail(email)) {
                throw authenticationError("An account with this email already exists under a different identity.");
            }

            user = new User();
            user.setGoogleSubject(googleSubject);
            user.setEmail(email);
            user.setRole(UserRole.LEARNER);
        } else {
            User emailOwner = userRepository.findByEmail(email).orElse(null);
            if (emailOwner != null && !emailOwner.getId().equals(user.getId())) {
                throw authenticationError("This email is already associated with another account.");
            }
            user.setEmail(email);
        }
        user.setDisplayName(oidcUser.getFullName());
        user.setProfileImageUrl(oidcUser.getPicture());

        return userRepository.save(user);
    }

    private OAuth2AuthenticationException authenticationError(String message) {
        return new OAuth2AuthenticationException(
                new OAuth2Error("invalid_user_identity"),
                message
        );
    }
}
