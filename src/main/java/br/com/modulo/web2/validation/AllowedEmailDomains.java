package br.com.modulo.web2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedEmailDomainsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedEmailDomains {
    String message() default "Domínio de email não permitido. Use: Gmail, Outlook, Yahoo, Hotmail, iCloud ou Bol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


