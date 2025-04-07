package com.example.imageProcessing.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
