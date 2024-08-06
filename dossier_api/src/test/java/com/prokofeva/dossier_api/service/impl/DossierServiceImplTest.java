package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.MessageCreatorFactory;
import com.prokofeva.dossier_api.service.SenderService;
import com.prokofeva.dossier_api.service.messageCreatorCmd.MessageCreatorFinReg;
import com.prokofeva.dto.EmailMessageDto;
import com.prokofeva.enums.Theme;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.yaml")
class DossierServiceImplTest {
//    @Mock
//    private JavaMailSender mailSender;
//    @Mock
//    private SenderService senderService;
    @Mock
    private MessageCreatorFactory messageCreatorFactory;
    @InjectMocks
    private DossierServiceImpl dossierService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Test
    void sendMessageToClient() throws MessagingException {
        dossierService.setEmailFrom(emailFrom);
        UUID uuid = UUID.randomUUID();
        EmailMessageDto emailMessageDto = new EmailMessageDto(
                "emailToForTest@mail.ru",
                Theme.FINISH_REGISTRATION,
                uuid
        );
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(System.getProperties()));

//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        MessageCreatorFinReg creatorFinReg = Mockito.mock(MessageCreatorFinReg.class);
//        when(messageCreatorFactory.getCreator(any(),anyString())).thenReturn(creatorFinReg);
//
//        dossierService.sendMessageToClient(emailMessageDto,"fceaf46f-08f4-462f-9267-cc03047835a5");
//
//        verify(senderService, times(1)).sendMailMessage(mimeMessage,"fceaf46f-08f4-462f-9267-cc03047835a5");
//        verify(mailSender,times(1)).createMimeMessage();
//        verify(messageCreatorFactory, times(1)).getCreator(any(Theme.class),anyString());
//
//        assertEquals(mimeMessage.getSubject(),Theme.FINISH_REGISTRATION.getValue());
//        assertEquals(mimeMessage.getAllRecipients().length,1 );
    }
}