package com.solano.ecommerce.bodegabackend.config;

import com.solano.ecommerce.bodegabackend.model.User;
import com.solano.ecommerce.bodegabackend.model.enums.AuthProvider;
import com.solano.ecommerce.bodegabackend.model.enums.Role;
import com.solano.ecommerce.bodegabackend.repository.UserRepository;
import com.solano.ecommerce.bodegabackend.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Obtener los detalles del usuario autenticado
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // Buscar o crear el usuario en la base de datos
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .fullName(name)
                    .role(Role.CLIENT)
                    .authProvider(AuthProvider.GOOGLE)
                    .password(null)
                    .build();
            return userRepository.save(newUser);
        });
        String jwtToken = jwtService.generateToken(user);

        String targetUrl = frontendUrl + "/auth/login?token=" + jwtToken;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
