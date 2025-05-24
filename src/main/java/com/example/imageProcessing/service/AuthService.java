package com.example.imageProcessing.service;


import com.example.imageProcessing.dto.UserDto;
import com.example.imageProcessing.entity.Roles;
import com.example.imageProcessing.entity.UserEntity;
import com.example.imageProcessing.exceptionHandling.InvalidEntryException;
import com.example.imageProcessing.exceptionHandling.UserNotFoundException;
import com.example.imageProcessing.repo.RolesRepo;
import com.example.imageProcessing.repo.UserRepo;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RolesRepo rolesRepo;

    public UserEntity signup(UserDto.Register register) {
        UserEntity user = new UserEntity();
        user.setUsername(register.getUsername());

        if(register.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            user.setPassword(passwordEncoder.encode(register.getPassword()));
        }else {
            throw new InvalidEntryException("password is invalid");
        }
        user.setEmail(register.getEmail());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationTimeExpire(LocalDateTime.now().plusMinutes(20));

        Roles userRole = (Roles) rolesRepo.findByName("user")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRoles(Set.of(userRole));
        sendVerificationEmail(user);

        return userRepo.save(user);

    }


    public UserEntity authenticate(UserDto.Login login) {
        UserEntity user = userRepo.findByEmail(login.getEmail()).orElseThrow(()-> new UserNotFoundException("User does not exists"));


        if(!user.isEnabled()){
            throw new RuntimeException("account is not yet verified");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                )
        );

        return userRepo.findByEmail(login.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
    }


    public void verify(UserDto.Verify userDto) {
        Optional<UserEntity> userOptional = userRepo.findByEmail(userDto.getEmail());

        if(userOptional.isPresent()){
            UserEntity user = userOptional.get();
            if(user.getVerificationTimeExpire().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code expired");
            }
            if(user.getVerificationCode().equals(userDto.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationTimeExpire(null);
                userRepo.save(user);
            }
            else{
                throw new RuntimeException("Invalid verification code");
            }
        }
        else{
            throw new RuntimeException("User not found");
        }

    }

    public void resend(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        email = email.trim();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        try {
            String finalEmail = email;
            UserEntity user = userRepo.findByEmail(finalEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + finalEmail));

            if (user.isEnabled()) {
                throw new RuntimeException("User already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationTimeExpire(LocalDateTime.now().plusMinutes(60));
            sendVerificationEmail(user);

            userRepo.save(user);
        } catch (Exception e) {

            throw new RuntimeException("Could not complete resend request");
        }
        }

    public void sendVerificationEmail(UserEntity user){
        String subject = "Account verified";
        String verificationCode = user.getVerificationCode();
        String content = "Hello " + user.getUsername() + ",\n\n"
                + "Thank you for registering. Please verify your account using the code below:\n\n"
                + "Verification Code: " + verificationCode + "\n\n"
                + "Or click the link below to verify your email:\n"
                + "https://youtu.be/dQw4w9WgXcQ?si=G7fGt89QEhDXrmYO" + verificationCode + "\n\n"
                + "This code expires in 15 minutes.\n\n"
                + "Best Regards,\nYour Website Team";


        try{
            emailService.sendVerificationEmail(user.getEmail() , subject , content);
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }

    private String generateVerificationCode(){
        Random random = new Random();

        int code  = random.nextInt(900000)+100000;

        return String.valueOf(code);
    }

}
