
package com.example.matrix_java_mentor_2.service.impl;

import com.example.matrix_java_mentor_2.entity.RoleEntity;
import com.example.matrix_java_mentor_2.entity.UserEntity;
import com.example.matrix_java_mentor_2.exception.BusinessRuleException;
import com.example.matrix_java_mentor_2.exception.UserAlreadyExistsException;
import com.example.matrix_java_mentor_2.model.ChangePasswordRequestDto;
import com.example.matrix_java_mentor_2.model.ChangeRolesRequestDto;
import com.example.matrix_java_mentor_2.model.UserRequestDto;
import com.example.matrix_java_mentor_2.model.UserResponseDto;
import com.example.matrix_java_mentor_2.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addUser(UserRequestDto dto) {

        log.info("Adding user: {}", dto.getUserName());

        if (userRepository.existsByUserName(dto.getUserName())) {
            log.error("Username already exists: {}", dto.getUserName());
            throw new UserAlreadyExistsException("Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUserName(dto.getUserName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        List<String> roles = dto.getRoles() == null || dto.getRoles().isEmpty()
                ? List.of("USER")
                : dto.getRoles().stream()
                .map(String::toUpperCase)
                .distinct()
                .toList();

        user.setRoles(roles.stream().map(name -> {
            RoleEntity role = new RoleEntity();
            role.setRoleName(name);
            role.setUser(user);
            return role;
        }).toList());

        userRepository.save(user);

        log.info("User added successfully: {}", dto.getUserName());
    }

    // Get current user
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(String username) {

        log.info("Getting current user: {}", username);

        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        List<String> roles = user.getRoles().stream()
                .map(RoleEntity::getRoleName)
                .toList();

        log.info("Current user found: {}", username);

        return new UserResponseDto(user.getId(), username, roles);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequestDto dto) {

        log.info("Changing password: {}", username);

        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            log.error("Incorrect old password for: {}", username);
            throw new BusinessRuleException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully: {}", username);
    }


    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {

        log.info("Getting all users");

        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUserName(),
                        user.getRoles().stream()
                                .map(RoleEntity::getRoleName)
                                .toList()
                ))
                .toList();

        log.info("Found {} users", users.size());

        return users;
    }

    @Transactional
    public UserResponseDto changeUserRoles(Long id, ChangeRolesRequestDto dto) {

        log.info("Changing roles for user ID: {}", id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<String> roles = dto.getRoles().stream()
                .map(String::toUpperCase)
                .distinct()
                .toList();

        if (roles.stream().anyMatch(role ->
                !List.of("USER", "ADMIN").contains(role))) {
            log.error("Invalid roles for user ID: {}", id);
            throw new BusinessRuleException("Invalid role");
        }

        user.getRoles().clear();

        user.getRoles().addAll(roles.stream().map(name -> {
            RoleEntity role = new RoleEntity();
            role.setRoleName(name);
            role.setUser(user);
            return role;
        }).toList());

        userRepository.save(user);

        log.info("Roles changed successfully for user ID: {}", id);

        return new UserResponseDto(user.getId(), user.getUserName(), roles);
    }

    @Transactional
    public void deleteUser(Long id, String currentUsername) {

        log.info("Deleting user ID: {}", id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getUserName().equals(currentUsername)) {
            log.error("Admin cannot delete themselves");
            throw new BusinessRuleException("Admin cannot delete themselves");
        }

        userRepository.delete(user);

        log.info("User deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        log.info("Loading user: {}", username);

        UserEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("User loaded successfully: {}", username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(
                                "ROLE_" + role.getRoleName()))
                        .toList())
                .build();
    }
}
