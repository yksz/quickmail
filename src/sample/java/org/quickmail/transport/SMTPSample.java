package org.quickmail.transport;

import java.io.IOException;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.TestParameters;

public class SMTPSample {
    private static final String HOST       = TestParameters.SMTP_HOST;
    private static final String TO_ADDRESS = TestParameters.SMTP_TO_ADDRESS;

    public static void main(String[] args) throws MessagingException, IOException {
        try (SMTP smtp = new SMTP()) {
            smtp.connect(HOST);
            smtp.send(new Mail()
                    .setFrom("no-reply@example.org")
                    .addTo(TO_ADDRESS)
                    .setSubject("SMTP Sample"));
        }
    }
}
