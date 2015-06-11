package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.Parameters;

public class IMAPSample {
    private static final String HOST     = Parameters.IMAP_HOST;
    private static final String USER     = Parameters.IMAP_USER;
    private static final String PASSWORD = Parameters.IMAP_PASSWORD;

    private static final int MAX_NUM = 3;

    public static void main(String[] args) throws MessagingException {
        try (IMAP imap = new IMAP()){
            imap.connect(HOST, USER, PASSWORD);
            int msgCount = imap.getMessageCount();
            for (int msgNum = msgCount; msgNum > msgCount - MAX_NUM; msgNum--) {
                Mail mail = imap.retrieveMail(msgNum);
                System.out.println(mail);
            }
        }
    }
}
