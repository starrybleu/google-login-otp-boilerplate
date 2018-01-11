package com.example.auth.rest.controller;

import com.example.auth.config.session.SessionConstants;
import com.example.auth.model.GoogleUser;
import com.example.auth.rest.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MainRestController {

    private HttpSession httpSession;
    private UserService userService;

    @GetMapping("/me")
    public Map<String, Object> me() {
        Map<String, Object> response = new LinkedHashMap<>();
        try {
            response.put("loginUser", userService.getOrSave((GoogleUser) httpSession.getAttribute(SessionConstants.GOOGLE_USER)));
            response.put("profile", httpSession.getAttribute(SessionConstants.GOOGLE_USER));
        } catch (Exception e) {
            response.put("exception", e.getMessage());
        } finally {
            return response;
        }
    }

}
