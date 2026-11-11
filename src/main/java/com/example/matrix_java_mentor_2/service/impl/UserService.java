package com.example.matrix_java_mentor_2.service.impl;

import com.example.matrix_java_mentor_2.entity.RoleEntity;
import com.example.matrix_java_mentor_2.entity.UserEntity;
import com.example.matrix_java_mentor_2.exception.BusinessRuleException;
import com.example.matrix_java_mentor_2.exception.DuplicateResourceException;
import com.example.matrix_java_mentor_2.model.UserRequestDto;
import com.example.matrix_java_mentor_2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Set<String> ALLOWED_ROLES = Set.of("USER", "ADMIN");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
                        .toList())
                .build();
    }

    @Transactional
    public void addUser(UserRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUserName())) {
            log.warn("User with duplicate user name was requested");
            throw new DuplicateResourceException("User with this user name already exists");
        }

        List<String> roleNames = (dto.getRoles() == null || dto.getRoles().isEmpty())
                ? List.of("USER")
                : dto.getRoles().stream().map(String::toUpperCase).distinct().toList();

        for (String roleName : roleNames) {
            if (!ALLOWED_ROLES.contains(roleName)) {
                log.warn("Invalid role requested: {}", roleName);
                throw new BusinessRuleException("Role must be one of: USER, ADMIN");
            }
        }

        UserEntity user = new UserEntity();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        for (String roleName : roleNames) {
            RoleEntity role = new RoleEntity();
            role.setRoleName(roleName);
            role.setUser(user);
            user.getRoles().add(role);
        }

        userRepository.save(user);
        log.info("User created: {}", user.getUserName());
    }
}
