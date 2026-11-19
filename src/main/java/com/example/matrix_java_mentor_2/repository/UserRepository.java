package com.example.matrix_java_mentor_2.repository;

import com.example.matrix_java_mentor_2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
    boolean existsByUserName(String userName);

}
