package br.com.modulo.web2.service.impl;

import br.com.modulo.web2.dto.request.UserRequest;
import br.com.modulo.web2.dto.response.UserResponse;
import br.com.modulo.web2.entity.Role;
import br.com.modulo.web2.entity.Usuario;
import br.com.modulo.web2.execption.errosApi.*;
import br.com.modulo.web2.mapper.UserMapper;
import br.com.modulo.web2.repository.UserRepository;
import br.com.modulo.web2.service.CpfValidationService;
import br.com.modulo.web2.service.EmailService;
import br.com.modulo.web2.service.RoleService;
import br.com.modulo.web2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CpfValidationService cpfValidationService;
    private final RoleService roleService;
    private final EmailService emailService;
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#result.id")
    })
    public UserResponse createUser(UserRequest userRequest) {
        if (!cpfValidationService.isValid(userRequest.getCpf())) {
            throw new InvalidCpfException("CPF inválido: " + userRequest.getCpf());
        }

        validateUniqueConstraints(userRequest);

        Usuario user = Usuario.builder()
                .nome(userRequest.getNome())
                .email(userRequest.getEmail().toLowerCase())
                .cpf(cpfValidationService.formatCpf(userRequest.getCpf()))
                .telefone(userRequest.getTelefone())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .active(true)
                .emailVerified(false)
                .verificationToken(generateVerificationToken())
                .tokenExpiryDate(LocalDateTime.now().plusHours(24))
                .build();

        Role userRole;
        try {
            userRole = roleService.findByName(Role.RoleName.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Role ROLE_USER não encontrada"));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
        user.addRole(userRole);

        Usuario savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(savedUser);

        return mapToResponseDTO(savedUser);
    }

    private void validateUniqueConstraints(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + userRequest.getEmail());
        }

        if (userRepository.existsByCpf(userRequest.getCpf())) {
            throw new CpfAlreadyExistsException("CPF já cadastrado: " + userRequest.getCpf());
        }

        if (userRepository.existsByTelefone(userRequest.getTelefone())) {
            throw new PhoneAlreadyExistsException("Telefone já cadastrado: " + userRequest.getTelefone());
        }
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

@Override
    @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findByActiveTrue(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));
        return mapToResponseDTO(user);
    }

    @Override
    @Cacheable(value = "user", key = "#email")
    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email) {
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com email: " + email));
        return mapToResponseDTO(user);
    }
    @Transactional(readOnly = true)
    public Optional<Usuario> findByCpf(String cpf) {
        log.debug("Searching user by CPF: {}", cpf);
        return userRepository.findByCpf(cpf);
    }
    @Transactional(readOnly = true)
    public Optional<Usuario> findByTelefone(String telefone) {
        log.debug("Searching user by phone: {}", telefone);
        return userRepository.findByTelefone(telefone);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id")
    })
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));

        validateUpdateConstraints(user, userRequest);

        user.setNome(userRequest.getNome());
        user.setTelefone(userRequest.getTelefone());

        if (!user.getEmail().equalsIgnoreCase(userRequest.getEmail())) {
            user.setEmail(userRequest.getEmail().toLowerCase());
            user.setEmailVerified(false);
            user.setVerificationToken(generateVerificationToken());
            user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
            emailService.sendVerificationEmail(user);
        }

        Usuario updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    private void validateUpdateConstraints(Usuario existingUser, UserRequest userRequest) {
        if (!existingUser.getEmail().equalsIgnoreCase(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + userRequest.getEmail());
        }

        if (!existingUser.getTelefone().equals(userRequest.getTelefone()) &&
                userRepository.existsByTelefone(userRequest.getTelefone())) {
            throw new PhoneAlreadyExistsException("Telefone já cadastrado: " + userRequest.getTelefone());
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id")
    })
    public void deleteUser(Long id) {
        Usuario user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean verifyEmail(String token) {
        Usuario user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token de verificação inválido"));

        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token de verificação expirado");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return true;
    }

    @Override
    public void resendVerificationEmail(String email) {
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com email: " + email));

        user.setVerificationToken(generateVerificationToken());
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        emailService.sendVerificationEmail(user);
    }

    private UserResponse mapToResponseDTO(Usuario user) {
        return UserResponse.builder()
                .id(user.getId())
                .nome(user.getNome())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .telefone(user.getTelefone())
                .active(user.getActive())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(java.util.stream.Collectors.toSet()))
                .build();
    }
}

