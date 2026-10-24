package com.thesdestudio.auth;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final ApplicationUserProvisionngService applicationUserProvisionngService;

    public CustomOidcUserService(ApplicationUserProvisionngService applicationUserProvisionngService) {
        this.applicationUserProvisionngService = applicationUserProvisionngService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        applicationUserProvisionngService.provisionUser(oidcUser);
        return oidcUser;
    }
}
