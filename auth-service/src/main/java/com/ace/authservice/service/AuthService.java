package com.ace.authservice.service;

import com.ace.authservice.dto.LoginRequest;
import com.ace.authservice.dto.LoginResponse;
import com.ace.authservice.dto.RegisterRequest;
import com.ace.authservice.dto.RegisterResponse;
import com.ace.authservice.entity.User;
import com.ace.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private static final String N8N_WEBHOOK_URL =
            "http://localhost:5678/webhook/email-verify";

    private final RestTemplate restTemplate = new RestTemplate();

    // ----------------- LOGIN -----------------
    public LoginResponse login(LoginRequest request) {
        log.info("Tentative de connexion avec email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            log.warn("⚠️ Aucun utilisateur trouvé avec cet email: {}", request.getEmail());
            log.debug("📦 Utilisateurs existants dans la base: {}", userRepository.findAll());
            return new LoginResponse(" Utilisateur non trouvé", false);
        }

        if (user.getStatus() != null && !"ACTIVE".equals(user.getStatus())) {
            log.warn("⚠️ Utilisateur {} non actif (status={})", user.getEmail(), user.getStatus());
            return new LoginResponse(" Email non vérifié", false);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn(" Mot de passe incorrect pour {}", user.getEmail());
            return new LoginResponse(" Mot de passe incorrect", false);
        }

        log.info(" Connexion réussie pour {}", user.getEmail());
        return new LoginResponse("Connexion réussie, bienvenue " + user.getFullName(), true);
    }

    // ----------------- REGISTER -----------------
    public RegisterResponse register(RegisterRequest request) {

        // 1. Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterResponse(" Cet email est déjà utilisé", false);
        }

        String token = UUID.randomUUID().toString();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .status("PENDING")
                .verificationToken(token)
                .build();

        userRepository.save(user);

        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("to", request.getEmail());
            payload.put("subject", "Vérification de votre compte");
            payload.put("message",
                    "Bonjour " + request.getFullName() + ",<br><br>"
                            + "Merci de vous être inscrit sur Interior Design App.<br>"
                            + "Cliquez sur ce lien pour activer votre compte :<br><br>"
                            + "<a href=\"http://localhost:8081/auth/verify?token=" + token + "\">"
                            + "Vérifier mon compte</a>");

            restTemplate.postForEntity(N8N_WEBHOOK_URL, payload, Void.class);
            log.info("Email de vérification envoyé à {}", request.getEmail());

        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du mail de vérification", e);
            return new RegisterResponse(
                    "Utilisateur créé mais l'email de vérification n'a pas pu être envoyé.",
                    true
            );
        }

        return new RegisterResponse(" Compte créé. Vérifiez votre email pour l'activer.", true);
    }


    // ----------------- VERIFY -----------------
    public String verify(String token) {

        Optional<User> optionalUser = userRepository.findByVerificationToken(token);

        if (optionalUser.isEmpty()) {
            return " Lien de vérification invalide ou déjà utilisé.";
        }

        User user = optionalUser.get();

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            return " Ce compte est déjà activé.";
        }

        user.setStatus("ACTIVE");
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Compte vérifié avec succès. Vous pouvez maintenant vous connecter.";
    }
}

