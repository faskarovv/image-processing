package com.example.imageProcessing.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Image {

    @Id
    private Long id;
    private String name;
    private String type;


    @Lob
    @Column(name  = "imagedata" , length = 1000)
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "owner_id" , referencedColumnName = "user_id")
    private UserEntity owner;
}
