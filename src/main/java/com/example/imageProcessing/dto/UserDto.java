package com.example.imageProcessing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class UserDto {


        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Login{
            private String email;
            private String password;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Register{
            private String username;

            private String password;

            private String email;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static  class Verify{
            private String email;

            private String verificationCode;
        }

       @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ResendRequest{
            @NotNull
            private String email;

           @Override
           public String toString() {
               return "ResendRequest(email=" + this.email + ")";
           }
        }

}
