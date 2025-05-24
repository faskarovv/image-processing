package com.example.imageProcessing.controller;

import com.example.imageProcessing.dto.ImageDto;
import com.example.imageProcessing.repo.ImageRepo;
import com.example.imageProcessing.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {


    private ImageService imageService;


    @PostMapping("/upload")
    public ResponseEntity<ImageDto.Response> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String transformationType,
            Authentication authentication
            ){
        ImageDto.Response  response = imageService.uploadImage(file , transformationType , authentication);

        return ResponseEntity.ok(response);
    }
}
