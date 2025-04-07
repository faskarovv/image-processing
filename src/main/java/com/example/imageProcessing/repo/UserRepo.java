package com.example.imageProcessing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.imageProcessing.entity.UserEntity;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

   Optional<UserEntity > findByEmail(String email);
}
