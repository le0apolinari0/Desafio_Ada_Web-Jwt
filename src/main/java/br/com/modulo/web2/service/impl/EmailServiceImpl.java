package br.com.modulo.web2.service.impl;

import br.com.modulo.web2.entity.Usuario;
import br.com.modulo.web2.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendVerificationEmail(Usuario user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Verifique seu email - Sistema Web2");
            message.setText(
                    "Olá " + user.getNome() + ",\n\n" +
                            "Clique no link abaixo para verificar seu email:\n" +
                            "http://localhost:8080/api/email/verify?token=" + user.getVerificationToken() + "\n\n" +
                            "Este link expira em 24 horas.\n\n" +
                            "Atenciosamente,\nEquipe Web2"
            );

            mailSender.send(message);
            log.info("Email de verificação enviado para: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Falha ao enviar email de verificação para: {}", user.getEmail(), e);
        }
    }
}

