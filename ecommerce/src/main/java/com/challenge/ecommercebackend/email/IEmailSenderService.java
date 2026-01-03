package com.challenge.ecommercebackend.email;

import jakarta.servlet.http.HttpServletRequest;

public interface IEmailSenderService {
    void sendRecoveryCode(String email, Long id, String code, HttpServletRequest url, int minutes);

    void sendPasswordRecoveryConfirmation(String email);

    void sendEmail(String email, String subject, String message);
}
