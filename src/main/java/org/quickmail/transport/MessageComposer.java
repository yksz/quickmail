package org.quickmail.transport;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quickmail.Mail;

class MessageComposer {
    public Message compose(Mail mail, Session session) throws MessagingException {
        Objects.requireNonNull("mail must not be null");
        Objects.requireNonNull("session must not be null");
        Message message = new MimeMessage(session);
        if (mail.getFrom() != null) {
            message.setFrom(new InternetAddress(mail.getFrom()));
        }
        if (mail.getTo() != null) {
            message.setRecipients(RecipientType.TO, convertToAddresses(mail.getTo()));
        }
        if (mail.getCc() != null) {
            message.setRecipients(RecipientType.CC, convertToAddresses(mail.getCc()));
        }
        if (mail.getBcc() != null) {
            message.setRecipients(RecipientType.BCC, convertToAddresses(mail.getBcc()));
        }
        if (mail.getReplyTo() != null) {
            message.setReplyTo(convertToAddresses(mail.getReplyTo()));
        }
        message.setSentDate((mail.getSentDate() != null) ? mail.getSentDate() : new Date());
        message.setSubject(mail.getSubject());
        setContent(message, mail);
        return message;
    }

    private Address[] convertToAddresses(List<String> strs) throws AddressException {
        return strs.stream()
                .map(str -> {
                    try {
                        return new InternetAddress(str);
                    } catch (AddressException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Address[]::new);
    }

    private void setContent(Message message, Mail mail) throws MessagingException {
        message.setText("");
    }
}
