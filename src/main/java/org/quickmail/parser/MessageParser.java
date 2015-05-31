package org.quickmail.parser;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.quickmail.Mail;

public interface MessageParser {
    Mail parse(Message message) throws MessagingException;
}
