package org.quickmail.parser;

import javax.mail.Message;

import org.quickmail.Mail;

public interface MessageParser {
    Mail parse(Message message) throws MessageParseException;
}
