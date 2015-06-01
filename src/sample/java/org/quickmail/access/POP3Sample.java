package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;
import org.quickmail.TestParameters;

public class POP3Sample {
    private static final String HOST     = TestParameters.POP3_HOST;
    private static final String USER     = TestParameters.POP3_USER;
    private static final String PASSWORD = TestParameters.POP3_PASSWORD;

    public static void main(String[] args) throws MessagingException {
        try (POP3 pop3 = new POP3()){
            pop3.connect(HOST, USER, PASSWORD);
            int msgCount = pop3.getMessageCount();
            for (int msgNum = 1; msgNum <= msgCount; msgNum++) {
                Mail mail = pop3.retrieveMessage(msgNum);
                System.out.println(mail);
            }
        }
    }
}
