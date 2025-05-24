package com.example.imageProcessing.controller;


import com.example.imageProcessing.dto.LoginResponse;
import com.example.imageProcessing.dto.UserDto;
import com.example.imageProcessing.entity.UserEntity;
import com.example.imageProcessing.service.AuthService;
import com.example.imageProcessing.service.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

        private  JwtService jwtService;
        private  AuthService authService ;


        @PostMapping("/signup")
        public ResponseEntity<?> register(@Valid @RequestBody UserDto.Register register){
            UserEntity registeredUser = authService.signup(register);

            return ResponseEntity.ok(registeredUser);
        }

         @PostMapping("/login")
         public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserDto.Login login){
                UserEntity authenticatedUser = authService.authenticate(login);

                String jwtToken = jwtService.generateToken(authenticatedUser);

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(jwtToken);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());

                return ResponseEntity.ok(loginResponse);
            }

            @PostMapping("/verify")
            public ResponseEntity<?> verify(@Valid @RequestBody UserDto.Verify verify){
                try{
                    authService.verify(verify);
                    return ResponseEntity.ok("Account verified successfuly");
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }

            @PostMapping("/resend")
            public ResponseEntity<?> resendVerificationCode(@Valid @RequestBody UserDto.ResendRequest email){

                System.out.println("DEBUG - Request object class: " + email.getClass().getName());
                System.out.println("DEBUG - Request object fields: " + Arrays.toString(email.getClass().getDeclaredFields()));

                log.info("Resend request received: {}", email); // Add this line
                log.info("Email extracted: {}", email.getEmail());

                try{
                    authService.resend(email.getEmail());
                    return ResponseEntity.ok("Email have been resended");
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }



}
