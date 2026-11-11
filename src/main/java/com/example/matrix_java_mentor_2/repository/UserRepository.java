package com.example.matrix_java_mentor_2.repository;

import com.example.matrix_java_mentor_2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.userName = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("select count(u) > 0 from UserEntity u where u.userName = :username")
    boolean existsByUsername(@Param("username") String username);
}
