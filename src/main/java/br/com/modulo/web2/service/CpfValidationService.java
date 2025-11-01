package br.com.modulo.web2.service;


public interface CpfValidationService {
    boolean isValid(String cpf);
    String formatCpf(String cpf);
}
