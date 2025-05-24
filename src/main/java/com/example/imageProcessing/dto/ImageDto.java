package com.example.imageProcessing.dto;

import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
public class ImageDto {

    @Data
    @Builder
    public static class Response{
        private UUID id;
        private String name;
        private String contentType;
        private Long fileSize;
        private LocalDateTime uploadTime;
        private String transformationType;
    }


    @Data
    @Builder
    public static class Download{
        private Resource resource;
        private String filename;
        private String contentType;
    }
}
