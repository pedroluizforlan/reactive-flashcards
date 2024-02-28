package com.pedroluizforlan.rectiveflashcards.domain.service;

import com.pedroluizforlan.rectiveflashcards.domain.dto.MailMessageDTO;
import com.pedroluizforlan.rectiveflashcards.domain.mapper.MailMapper;
import com.pedroluizforlan.rectiveflashcards.helper.RetryHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;
    private final String sender;
    private final RetryHelper retryHelper;

    public MailService(final JavaMailSender mailSender,
                       final TemplateEngine templateEngine,
                       final MailMapper mailMapper,
                       final RetryHelper retryHelper,
                       @Value("${rective-flashcards.mail.sender}") final String sender) {

        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.mailMapper = mailMapper;
        this.retryHelper = retryHelper;
        this.sender = sender;
    }


    public Mono<Void> send(final MailMessageDTO mailMessageDTO) {
        return Mono.just(mailSender.createMimeMessage())
                .flatMap(mimeMessage -> buildMessage(mimeMessage, mailMessageDTO))
                .flatMap(this::send)
                .then();
    }

    private Mono<MimeMessage> buildMessage(MimeMessage mimeMessage, MailMessageDTO mailMessageDTO) {
        return Mono.fromCallable(() -> {
            var helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            mailMapper.toMimeMessageHelper(helper, mailMessageDTO, sender, buildTemplate(mailMessageDTO.template(), mailMessageDTO.variables()));
            return mimeMessage;
        });
    }

    private String buildTemplate(final String template, final Map<String, Object> variables) {
        var context = new Context(new Locale("pt", "BR"));
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

    private Mono<Void> send(final MimeMessage mimeMessage) {
        return Mono.fromCallable(() -> {
            mailSender.send(mimeMessage);
            return mimeMessage;
        }).retryWhen(retryHelper.processRetry(UUID.randomUUID().toString(), throwable -> throwable instanceof MailException)).then();
    }
}
