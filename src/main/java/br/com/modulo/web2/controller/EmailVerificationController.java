package br.com.modulo.web2.controller;

import br.com.modulo.web2.dto.response.ApiResponse;
import br.com.modulo.web2.entity.Usuario;
import br.com.modulo.web2.service.EmailVerificationService;
import br.com.modulo.web2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "Verificação de Email", description = "Endpoints para verificação e gestão de emails")
public class EmailVerificationController {

    private final UserService userService;

    @PostMapping("/verify")
    @Operation(summary = "Verificar email", description = "Valida o token de verificação de email.")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Parameter(description = "Token de verificação") @RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("Email verificado com sucesso", null));
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Reenviar verificação", description = "Reenvia o email de verificação.")
    public ResponseEntity<ApiResponse<Void>> resendVerification(
            @Parameter(description = "Email do usuário") @RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Email de verificação reenviado", null));
    }

}
