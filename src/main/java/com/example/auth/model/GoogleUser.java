package com.example.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.auth.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUser implements Serializable {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }
}
