package com.ace.authservice;

import com.ace.authservice.entity.User;
import com.ace.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitUserLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "amine@example.com";

        if (userRepository.findByEmail(email).isEmpty()) {
            User user = User.builder()
                    .fullName("Amine From Spring")
                    .email(email)
                    .password(passwordEncoder.encode("123456"))
                    .role("USER")
                    .build();

            userRepository.save(user);
            System.out.println(" Utilisateur créé par Spring : " + email);
        } else {
            System.out.println(" Utilisateur déjà présent.");
        }
    }
}
