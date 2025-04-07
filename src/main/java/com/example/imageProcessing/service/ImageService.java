package com.example.imageProcessing.service;


import com.example.imageProcessing.repo.ImageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageService {

    private ImageRepo imageRepo;
}
