package com.example.imageProcessing.service;


import com.example.imageProcessing.dto.ImageDto;
import com.example.imageProcessing.entity.Image;
import com.example.imageProcessing.entity.UserEntity;
import com.example.imageProcessing.exceptionHandling.UserNotFoundException;
import com.example.imageProcessing.repo.ImageRepo;
import com.example.imageProcessing.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepo imageRepo;
    private UserRepo userRepo;


    public ImageDto.Response uploadImage(
            MultipartFile file , String transformationtype , Authentication authentication
    ){
        UserEntity user = userRepo.findByUsername(authentication.getName()).orElseThrow(()-> new UserNotFoundException("User was not found"));

        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .uploadTime(LocalDateTime.now())
                .transformationType(transformationtype)
                .owner(user)
                .build();

        try{
            image.setImageData(file.getBytes());
            Image savedImage = imageRepo.save(image);


            return ImageDto.Response.builder()
                    .id(image.getId())
                    .name(image.getName())
                    .contentType(image.getContentType())
                    .fileSize(image.getFileSize())
                    .transformationType(image.getTransformationType())
                    .uploadTime(image.getUploadTime())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image",e);
        }
    }
}
