package br.com.modulo.web2.service.impl;

import br.com.modulo.web2.service.CpfValidationService;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
public class CpfValidationServiceImpl implements CpfValidationService {

    @Override
    public boolean isValid(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }

            int firstDigit = 11 - (sum % 11);
            if (firstDigit > 9) firstDigit = 0;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }

            int secondDigit = 11 - (sum % 11);
            if (secondDigit > 9) secondDigit = 0;

            return Character.getNumericValue(cpf.charAt(9)) == firstDigit &&
                    Character.getNumericValue(cpf.charAt(10)) == secondDigit;

        } catch (InputMismatchException e) {
            return false;
        }
    }

    @Override
    public String formatCpf(String cpf) {
        if (cpf == null) return null;
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

}

