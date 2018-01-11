package com.example.auth.rest.controller;

import com.example.auth.config.session.SessionConstants;
import com.example.auth.domain.user.User;
import com.example.auth.model.GoogleUser;
import com.example.auth.rest.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@AllArgsConstructor
public class MainController {

    private HttpSession httpSession;
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute(SessionConstants.LOGIN_USER, userService.getOrSave((GoogleUser) httpSession.getAttribute(SessionConstants.GOOGLE_USER)));
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        } finally {
            return "index";
        }
    }

}
