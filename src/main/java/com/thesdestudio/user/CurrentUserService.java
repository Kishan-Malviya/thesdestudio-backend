package com.thesdestudio.user;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(String googleSubject) {
        return userRepository
                .findByGoogleSubject(googleSubject)
                .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Authenticated user was not found in the application database."
                                )
                );
    }
}
