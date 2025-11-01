package br.com.modulo.web2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.regex.Pattern;

public class AllowedEmailDomainsValidator implements ConstraintValidator<AllowedEmailDomains, String> {

    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "gmail.com",
            "outlook.com",
            "yahoo.com",
            "hotmail.com",
            "icloud.com",
            "bol.com.br",
            "live.com",
            "msn.com"
    );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }

        String domain = extractDomain(email);
        return ALLOWED_DOMAINS.contains(domain.toLowerCase());
    }

    private String extractDomain(String email) {
        int atIndex = email.lastIndexOf('@');
        return atIndex >= 0 ? email.substring(atIndex + 1).toLowerCase() : "";
    }
}
