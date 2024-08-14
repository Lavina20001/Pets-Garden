package com.pets.petsgarden.service;

import com.pets.petsgarden.entity.User;
import com.pets.petsgarden.repository.UserRepository;
import com.pets.petsgarden.response.AuthenticationResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private JwtService jwtService;
        @Autowired
        private AuthenticationManager authenticationManager;

        public AuthenticationResponse register(User request) {

            User user = new User();
            BeanUtils.copyProperties(request, user);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user = userRepository.save(user);

            String token = jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        }

        public AuthenticationResponse login(UserDetails request) {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.generateToken(user);

            return new AuthenticationResponse(token);
        }
    }


