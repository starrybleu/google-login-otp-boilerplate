package com.example.auth.rest.service;

import com.example.auth.domain.user.User;
import com.example.auth.domain.user.UserRole;
import com.example.auth.model.GoogleUser;
import com.example.auth.rest.repository.UserRepository;
import com.example.auth.rest.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;
    private UserRoleRepository roleRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User getUser(GoogleUser googleUser) {
        return userRepository.findUserByEmail(googleUser.getEmail());
    }

    @Transactional
    public User getOrSave(GoogleUser googleUser) throws Exception {
        checkValidDomain(googleUser);

        User savedUser = userRepository.findUserByEmail(googleUser.getEmail());

        if (savedUser == null) {
            User newUser = googleUser.toEntity();
            newUser.setGoogleSecretToPersist(Base32.random());
            savedUser = userRepository.save(newUser);
        }

        return savedUser;
    }

    public void checkValidDomain(GoogleUser googleUser) throws BadCredentialsException {
        String email = googleUser.getEmail();
        if (email == null || !email.split("@")[1].equals("gmail.com")) {
            throw new BadCredentialsException("invalid domain");
        }
    }

    public Map<String, String> getOtpKeyAndQRcodeMap(GoogleUser googleUser) {
        User user = userRepository.findUserByEmail(googleUser.getEmail());

        if (StringUtils.isEmpty(user.getGoogleSecret())) {
            user.setGoogleSecretToPersist(Base32.random());
            save(user);
        }

        String key = user.getGoogleSecret();
        String qrCodeUrl = getQrCodeUrl(googleUser.getEmail(), key);

        return new HashMap<String, String>() {{
            put("key", key);
            put("qrCodeUrl", qrCodeUrl);
        }};
    }

    public String getQrCodeUrl(String email, String secret) {
        String format = "http://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s?secret=%s";
        return String.format(format, email, secret);
    }

    public boolean checkCode(String secret, String otpCode) {
        Totp totp = new Totp(secret);
        return totp.verify(otpCode);
    }

    public void setRolesOn2FaSuccess(GoogleUser googleUser) {
        User user = userRepository.findUserByEmail(googleUser.getEmail());
        Set<UserRole> userRoles = roleRepository.findByUser(user);
        PreAuthenticatedAuthenticationToken newToken = buildAuthenticationTokenApplingNewRoles(userRoles);
        SecurityContextHolder.getContext().setAuthentication(newToken); // Grant user new roles managed by database
    }

    private PreAuthenticatedAuthenticationToken buildAuthenticationTokenApplingNewRoles(Collection<? extends GrantedAuthority> userRoles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new PreAuthenticatedAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), userRoles);
    }
}
