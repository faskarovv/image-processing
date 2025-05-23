package com.example.imageProcessing.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Data
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> userEntitySet = new HashSet<>();
}
