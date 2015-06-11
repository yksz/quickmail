package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.Parameters;

public class POP3Sample {
    private static final String HOST     = Parameters.POP3_HOST;
    private static final String USER     = Parameters.POP3_USER;
    private static final String PASSWORD = Parameters.POP3_PASSWORD;

    private static final int MAX_NUM = 3;

    public static void main(String[] args) throws MessagingException {
        try (POP3 pop3 = new POP3()){
            pop3.connect(HOST, USER, PASSWORD);
            int msgCount = pop3.getMessageCount();
            for (int msgNum = msgCount; msgNum > msgCount - MAX_NUM; msgNum--) {
                Mail mail = pop3.retrieveMail(msgNum);
                System.out.println(mail);
            }
        }
    }
}
