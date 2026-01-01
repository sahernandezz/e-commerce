package com.challenge.ecommercebackend.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class MailComponent {

    private final Logger logger = Logger.getLogger(MailComponent.class.getName());

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public MailComponent(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmailHtml(final String MailTo, final String subject, final Map<String, Object> props, final String templete) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariables(props);
            helper.setTo(MailTo);
            helper.setText(templateEngine.process(templete, context), true);
            helper.setSubject(subject);
            helper.setFrom(MailTo);
            emailSender.send(message);
        } catch (Exception e) {
            logger.info("Error sending email: " + e.getMessage());
        }
    }

    @Async
    public void sendEmail(final String MailTo, final String subject, final String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(MailTo);
            helper.setText(text);
            helper.setSubject(subject);
            emailSender.send(message);
        } catch (Exception e) {
            logger.info("Error sending email: " + e.getMessage());
        }
    }
}