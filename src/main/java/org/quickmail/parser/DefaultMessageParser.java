package org.quickmail.parser;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.quickmail.Mail;

public class DefaultMessageParser implements MessageParser {
    @Override
    public Mail parse(Message message) throws MessagingException {
        Objects.requireNonNull(message, "message must not be null");
        Mail mail = new Mail();
        mail.setFrom(parseFrom(message));
        mail.addTo(parseTo(message));
        mail.addCc(parseCc(message));
        mail.addBcc(parseBcc(message));
        mail.addReplyTo(parseReplyTo(message));
        mail.setSentDate(parseSentDate(message));
        mail.setSubject(parseSubject(message));
        return mail;
    }

    protected String parseFrom(Message message) throws MessagingException {
        InternetAddress[] from = (InternetAddress[]) message.getFrom();
        return (from == null) ? null : from[0].getAddress();
    }

    protected String[] parseTo(Message message) throws MessagingException {
        return findRecipients(message, RecipientType.TO);
    }

    protected String[] parseCc(Message message) throws MessagingException {
        return findRecipients(message, RecipientType.CC);
    }

    protected String[] parseBcc(Message message) throws MessagingException {
        return findRecipients(message, RecipientType.BCC);
    }

    private String[] findRecipients(Message message, RecipientType type) throws MessagingException {
        InternetAddress[] addrs = (InternetAddress[]) message.getRecipients(type);
        if (addrs == null) {
            return new String[0];
        }
        return Stream.of(addrs)
                .map(addr -> addr.getAddress())
                .toArray(String[]::new);
    }

    protected String[] parseReplyTo(Message message) throws MessagingException {
        InternetAddress[] addrs = (InternetAddress[]) message.getReplyTo();
        if (addrs == null) {
            return new String[0];
        }
        return Stream.of(addrs)
                .map(addr -> addr.getAddress())
                .toArray(String[]::new);
    }

    protected Date parseSentDate(Message message) throws MessagingException {
        return message.getSentDate();
    }

    protected String parseSubject(Message message) throws MessagingException {
        return message.getSubject();
    }
}
