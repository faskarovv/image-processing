package com.example.imageProcessing.controller;


import com.example.imageProcessing.dto.LoginResponse;
import com.example.imageProcessing.dto.UserDto;
import com.example.imageProcessing.entity.UserEntity;
import com.example.imageProcessing.service.AuthService;
import com.example.imageProcessing.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

        private  JwtService jwtService;
        private  AuthService authService ;


        @PostMapping("/signup")
        public ResponseEntity<?> register(@RequestBody UserDto.Register register){
            String password = register.getPassword();

            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Password cannot be null or empty");
            }

            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                return ResponseEntity.badRequest().body("Password does not meet the required complexity");
            }

            UserEntity registeredUser = authService.signup(register);

            return ResponseEntity.ok(registeredUser);
        }

         @PostMapping("/login")
         public ResponseEntity<LoginResponse> login(@RequestBody UserDto.Login login){
                UserEntity authenticatedUser = authService.authenticate(login);

                String jwtToken = jwtService.generateToken(authenticatedUser);

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(jwtToken);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());

                return ResponseEntity.ok(loginResponse);
            }

            @PostMapping("/verify")
            public ResponseEntity<?> verify(@RequestBody UserDto.Verify verify){
                try{
                    authService.verify(verify);
                    return ResponseEntity.ok("Account verified successfuly");
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }

            @PostMapping("/resend")
            public ResponseEntity<?> resendVerificationCode(@RequestBody String email){
                try{
                    authService.resend(email);
                    return ResponseEntity.ok("Email have been resended");
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }



}
