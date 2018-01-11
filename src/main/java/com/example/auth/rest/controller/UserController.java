package com.example.auth.rest.controller;

import com.example.auth.config.session.SessionConstants;
import com.example.auth.domain.user.User;
import com.example.auth.model.GoogleUser;
import com.example.auth.rest.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@AllArgsConstructor
public class UserController {

    private HttpSession httpSession;
    private UserService userService;

    @GetMapping("/2fa")
    public String totp(Model model, @SessionAttribute(SessionConstants.GOOGLE_USER) GoogleUser googleUser, HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            throw new RuntimeException("You're already logged in");
        }
        boolean isFirst2Fa = false;
        try {
            User user = userService.getUser(googleUser);
            httpSession.setAttribute(SessionConstants.LOGIN_USER, user);
            isFirst2Fa = StringUtils.isEmpty(user.getGoogleSecret());
        } catch (Exception e) {

        }

        return isFirst2Fa ? "redirect:/2fa-first" : "2fa";
    }

    @GetMapping("/2fa-first")
    public String totpFirst(Model model, @SessionAttribute(SessionConstants.GOOGLE_USER) GoogleUser googleUser) {
        Map<String, String> otpMap = userService.getOtpKeyAndQRcodeMap(googleUser);
        User user = userService.getUser(googleUser);

        user.setGoogleSecretToPersist(otpMap.get("key"));
        userService.save(user);

        model.addAttribute("key", user.getGoogleSecret());
        model.addAttribute("qrCodeUrl", otpMap.get("qrCodeUrl"));

        return "2fa-first";
    }

    @PostMapping("/2fa-authenticate")
    public String on2FaAuthentication(Model model, @SessionAttribute(SessionConstants.GOOGLE_USER) GoogleUser googleUser, HttpServletRequest request) {
        String otpCode = request.getParameter("otpCode");
        String encodedKey = request.getParameter("encodedKey");

        boolean checkCode = userService.checkCode(encodedKey, otpCode);

        if (checkCode) {
            userService.setRolesOn2FaSuccess(googleUser);
        } else {
            model.addAttribute("message", "OTP 코드가 틀렸습니다. 다시 시도해주세요.");
            return "error/500";
        }

        System.out.println("checkCode : " + checkCode);

        return checkCode ? "index" : "invalid-otp";
    }

}
