package com.example.imageProcessing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @NotNull
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String username;


    @NotBlank
    private String password;


    @NotBlank
    @Column(unique = true)
    @Email(message = "email is not valid")
    private String email;

    public boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name  = "verification_expiration")
    private LocalDateTime verificationTimeExpire;

    @ManyToMany(fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @JoinTable(name = "user_roles" ,
              joinColumns = @JoinColumn(name = "user_id"),
              inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Roles> roles = new HashSet<>();


    @OneToMany(mappedBy = "owner" , cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();


    public UserEntity(String username, String password, String email) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
