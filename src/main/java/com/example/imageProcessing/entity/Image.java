package com.example.imageProcessing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contentType; //like jpeg png or sum

    @Column(nullable = false)
    private Long fileSize;

    @Lob
    @Column(name  = "image_data" , columnDefinition = "BYTEA") //postgres special thing
    private byte[] imageData;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @Column(name = "original_image_id")
    private UUID originalImageId;

    @Column(name = "transformation_type")
    private String transformationType;

    @ManyToOne
    @JoinColumn(name = "owner_id" , referencedColumnName = "user_id")
    private UserEntity owner;
}
