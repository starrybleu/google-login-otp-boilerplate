package com.example.auth.config.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.auth.domain.user.User;
import com.example.auth.domain.user.UserRole;
import com.example.auth.model.GoogleUser;
import com.example.auth.rest.repository.UserRoleRepository;
import com.example.auth.rest.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Component
@AllArgsConstructor
public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HttpSession httpSession;
    private ObjectMapper objectMapper;
    private UserRoleRepository roleRepository;
    private UserService userService;

    // Todo : Sending an email or slack notification when an user has logged in.

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            GoogleUser googleUser = getGoogleUser(authentication);
            User user = userService.getOrSave(googleUser);

            logger.info("An user has logged in. email : {}", user.getEmail());

            httpSession.setAttribute(SessionConstants.LOGIN_USER, user);
            httpSession.setAttribute(SessionConstants.GOOGLE_USER, getGoogleUser(authentication));
        } catch (Exception e) {
            logger.warn("Failed to authentication. Exception : {} has occurred.", e.getClass().getName());
        } finally {
            response.sendRedirect("/2fa");
        }
    }

    private GoogleUser getGoogleUser(Authentication authentication) {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        return objectMapper.convertValue(oAuth2Authentication.getUserAuthentication().getDetails(), GoogleUser.class);
    }

}
