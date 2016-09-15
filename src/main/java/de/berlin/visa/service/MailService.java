package de.berlin.visa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailService {
    @Value("${mail.source}")
    private String sourceEmail;
    @Value("${mail.source.password}")
    private String sourceEmailPassword;
    @Value("${mail.dest}")
    private String destEmail;

    private Session session;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public MailService() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sourceEmail, sourceEmailPassword);
            }
        });
    }

    /**
     * Sends notification email about available appointment date.
     *
     * @param availableDay
     * @return true if email was successfully sent
     */
    public boolean send(String availableDay) {
        LOGGER.info("Sending email to {} from {} with {} as available date", destEmail, sourceEmail, availableDay);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sourceEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail));
            message.setSubject(availableDay + " is a closest available date");

            Transport.send(message);
            LOGGER.info("Email was sent");

            return true;
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.error("Notification email wasn't sent. Please, investigate the problem");
        return false;
    }
}
