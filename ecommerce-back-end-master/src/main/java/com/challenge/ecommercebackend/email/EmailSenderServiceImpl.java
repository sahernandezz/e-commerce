package com.challenge.ecommercebackend.email;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailSenderServiceImpl implements IEmailSenderService {

    private final MailComponent mail;

    @Value("${auth.email.recovery_code.url}")
    private String recoveryCodeUrl;

    @Value("${auth.email.url}")
    private String url;

    @Value("${auth.email.recovery_code.subject}")
    private String recoveryCodeSubject;

    @Value("${auth.email.recovery_confirmation.subject}")
    private String recoveryCodeConfirmationSubject;

    @Value("${auth.email.recovery_code.templete}")
    private String recoveryCodeTemplete;

    @Value("${auth.email.recovery_confirmation.templete}")
    private String recoveryCodeConfirmationTemplete;

    @Override
    public void sendRecoveryCode(
            final String email, final Long id, final String code,
            final HttpServletRequest url, final int minutes
    ) {
        Map<String, Object> model = new HashMap<>();
        model.put("recovery", code);
        model.put("minutes", minutes);
        model.put("url", this.url + this.recoveryCodeUrl + Base64.getEncoder().encodeToString(id.toString().getBytes()));
        mail.sendEmailHtml(email, this.recoveryCodeSubject, model, this.recoveryCodeTemplete);
    }

    @Override
    public void sendPasswordRecoveryConfirmation(final String email) {
        mail.sendEmailHtml(email, this.recoveryCodeConfirmationSubject, null, this.recoveryCodeConfirmationTemplete);
    }

    @Override
    public void sendEmail(final String email, final String subject, final String message) {
        mail.sendEmail(email, subject, message);
    }
}