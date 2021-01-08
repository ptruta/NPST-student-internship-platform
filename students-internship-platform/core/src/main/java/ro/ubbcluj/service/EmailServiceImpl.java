package ro.ubbcluj.service;

import ro.ubbcluj.exception.HMSMailException;
import ro.ubbcluj.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * This class contains all the business logic regarding sending emails
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Method sends emails
     *
     * @param email
     * @param sendFrom
     * @param subject
     * @param text
     * @throws HMSMailException
     */
    public void sendEmail(String email, String sendFrom, String subject, String text) throws HMSMailException {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email);
            helper.setFrom(sendFrom);
            helper.setSubject(subject);
            helper.setText(text);
        } catch (MessagingException e) {
            throw new HMSMailException("Message not correct!");
        }
        javaMailSender.send(mail);
    }
}