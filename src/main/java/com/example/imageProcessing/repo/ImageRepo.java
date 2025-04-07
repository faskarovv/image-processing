package com.example.imageProcessing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.imageProcessing.entity.Image;
import java.util.*;


public interface ImageRepo extends JpaRepository<Image , Long> {

    Optional<Image> findByName(String name);
}
