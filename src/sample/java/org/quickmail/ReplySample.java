package org.quickmail;

import java.io.IOException;

import javax.mail.MessagingException;

import org.quickmail.access.IMAP;
import org.quickmail.transport.SMTP;

public class ReplySample {
    private static final String IMAP_HOST     = Parameters.IMAP_HOST;
    private static final String IMAP_USER     = Parameters.IMAP_USER;
    private static final String IMAP_PASSWORD = Parameters.IMAP_PASSWORD;

    private static final String SMTP_HOST     = Parameters.SMTP_HOST;
    private static final String SMTP_USER     = Parameters.SMTP_USER;
    private static final String SMTP_PASSWORD = Parameters.SMTP_PASSWORD;

    public static void main(String[] args) throws MessagingException, IOException {
        try (IMAP imap = new IMAP(); SMTP smtp = new SMTP()) {
            imap.setSslEnabled(true);
            imap.connect(IMAP_HOST, IMAP_USER, IMAP_PASSWORD);
            int msgCount = imap.getMessageCount();
            if (msgCount > 0) {
                Mail mail = imap.retrieveMail(msgCount);
                mail.clearTo()
                        .addTo(mail.getReplyTo())
                        .setFrom("no-reply@example.org")
                        .setSubject("Re:" + mail.getSubject());
                smtp.setSslEnabled(true);
                smtp.connect(SMTP_HOST, SMTP_USER, SMTP_PASSWORD);
                smtp.send(mail);
            }
        }
    }
}
