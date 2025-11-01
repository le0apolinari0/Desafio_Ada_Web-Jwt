package br.com.modulo.web2.service;

import br.com.modulo.web2.entity.Usuario;
import br.com.modulo.web2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Usuario usuario = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado pelo email: " + email));

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getPassword())
                    .authorities(usuario.getRoles().stream()
                            .map(role -> "ROLE_" + role.getName())
                            .toArray(String[]::new))
                    .build();
        }

        public static class UserPrincipal {
            public static UserDetails create(Usuario usuario) {
                return User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPassword())
                        .authorities(usuario.getRoles().stream()
                                .map(role -> "ROLE_" + role.getName())
                                .toArray(String[]::new))
                        .build();
            }
        }
}


