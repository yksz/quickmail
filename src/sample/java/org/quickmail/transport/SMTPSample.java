package org.quickmail.transport;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.TestParameters;

public class SMTPSample {
    private static final String HOST       = TestParameters.SMTP_HOST;
    private static final String TO_ADDRESS = TestParameters.SMTP_TO_ADDRESS;

    public static void main(String[] args) throws MessagingException {
        Mail mail = new Mail();
        mail.setFrom("foo@bar.com");
        mail.addTo(TO_ADDRESS);
        mail.setSubject("SMTP Sample");
        try (SMTP smtp = new SMTP()) {
            smtp.connect(HOST);
            smtp.send(mail);
        }
    }
}
