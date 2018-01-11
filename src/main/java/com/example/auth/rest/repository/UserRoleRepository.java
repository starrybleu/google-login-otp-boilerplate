package com.example.auth.rest.repository;

import com.example.auth.domain.user.User;
import com.example.auth.domain.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Set<UserRole> findByUser(User user);

}
