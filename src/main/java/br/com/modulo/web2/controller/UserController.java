package br.com.modulo.web2.controller;

import br.com.modulo.web2.dto.request.UserRequest;
import br.com.modulo.web2.dto.response.ApiResponse;
import br.com.modulo.web2.dto.response.UserResponse;
import br.com.modulo.web2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações CRUD completas para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os usuários com paginação", description = "Retorna uma lista paginada de usuários. Apenas ADMIN tem acesso.")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Parameter(hidden = true) @PageableDefault(size = 11, sort = "nome") Pageable pageable) {
        Page<UserResponse> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico.")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Usuário encontrado com sucesso", user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("Usuário atualizado com sucesso", user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desativar usuário", description = "Realiza soft delete do usuário (apenas ADMIN).")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Usuário desativado com sucesso", null));
    }
}
