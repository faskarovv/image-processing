package com.example.imageProcessing.repo;

import com.example.imageProcessing.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles , Long> {

    Optional<Object> findByName(String roleUser);
}
