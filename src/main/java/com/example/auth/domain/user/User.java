package com.example.auth.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.example.auth.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String googleSecret;

    private String name;

    private Long custNo;

    private String phone;

    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private Set<UserRole> roles = new LinkedHashSet<>();

    @Builder
    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.active = true;
    }

    public void setGoogleSecretToPersist(String googleSecret) {
        this.googleSecret = googleSecret;
    }

//    public void addRoles(Set<UserRole> userRoles) {
//        if (roles == null) {
//            roles = new LinkedHashSet<>();
//        }
//        roles.addAll(userRoles);
//    }

    @Override
    public String toString() {
        return ToStringBuilder
                .reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
