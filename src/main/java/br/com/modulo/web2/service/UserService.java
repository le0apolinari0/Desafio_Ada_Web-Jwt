package br.com.modulo.web2.service;

import br.com.modulo.web2.dto.request.UserRequest;
import br.com.modulo.web2.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    Page<UserResponse> findAll(Pageable pageable);
    UserResponse findById(Long id);
    UserResponse findByEmail(String email);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
    boolean verifyEmail(String token);
    void resendVerificationEmail(String email);
}
