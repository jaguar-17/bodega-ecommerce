package com.solano.ecommerce.bodegabackend.service;

import com.solano.ecommerce.bodegabackend.dto.request.AuthenticationRequest;
import com.solano.ecommerce.bodegabackend.dto.request.RegisterRequest;
import com.solano.ecommerce.bodegabackend.dto.response.AuthenticationResponse;
import com.solano.ecommerce.bodegabackend.model.User;
import com.solano.ecommerce.bodegabackend.model.enums.AuthProvider;
import com.solano.ecommerce.bodegabackend.model.enums.Role;
import com.solano.ecommerce.bodegabackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.CLIENT)
                .authProvider(AuthProvider.LOCAL)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
