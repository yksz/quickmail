package org.quickmail.transport;

import java.io.IOException;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.Parameters;

public class SMTPSample {
    private static final String HOST       = Parameters.SMTP_HOST;
    private static final String TO_ADDRESS = Parameters.SMTP_TO_ADDRESS;

    public static void main(String[] args) throws MessagingException, IOException {
        try (SMTP smtp = new SMTP()) {
            smtp.connect(HOST);
            smtp.send(new Mail()
                    .addTo(TO_ADDRESS)
                    .setFrom("no-reply@example.org")
                    .setSubject("SMTP Sample"));
        }
    }
}
