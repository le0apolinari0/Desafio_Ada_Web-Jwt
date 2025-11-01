package br.com.modulo.web2.service;

import br.com.modulo.web2.entity.Usuario;


public interface EmailService {
    void sendVerificationEmail(Usuario user);
}
