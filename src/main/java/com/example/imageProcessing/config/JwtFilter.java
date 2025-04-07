package com.example.imageProcessing.config;

import com.example.imageProcessing.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    public JwtService jwtService;

//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver exceptionResolver;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader== null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request , response);
            return;
        }

        try{
            final String jwt =authHeader.substring(7);
            final String email = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            if(email!= null && authentication ==null){
                UserDetails userDetails =this.userDetailsService.loadUserByUsername(email);

                if(jwtService.isTokenValid(jwt , userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());

                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        } catch (Exception e) {
//            DefaultErrorAttributes exceptionResolver = null;
//            exceptionResolver.resolveException(request , response , null , e);
            System.out.println(e.getMessage());
        }

    }
}
