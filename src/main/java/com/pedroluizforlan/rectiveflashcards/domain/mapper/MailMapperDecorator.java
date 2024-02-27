package com.pedroluizforlan.rectiveflashcards.domain.mapper;

import com.pedroluizforlan.rectiveflashcards.domain.dto.MailMessageDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class MailMapperDecorator implements MailMapper {

    @Qualifier("delegate")
    @Autowired
    private MailMapper mailMapper;

    @Override
    public MimeMessageHelper toMimeMessageHelper(MimeMessageHelper mimeMessageHelper, MailMessageDTO mailMessageDTO, String sender, String body) throws MessagingException {
        mailMapper.toMimeMessageHelper(mimeMessageHelper, mailMessageDTO, sender, body);
        mimeMessageHelper.setText(body, true);
        return mimeMessageHelper;
    }
}
