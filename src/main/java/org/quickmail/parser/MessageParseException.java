package org.quickmail.parser;

import javax.mail.MessagingException;

public class MessageParseException extends MessagingException {
    private static final long serialVersionUID = 3085897683966711791L;

    public MessageParseException() {
        super();
    }

    public MessageParseException(String message) {
        super(message);
    }

    public MessageParseException(String message, Exception cause) {
        super(message, cause);
    }
}
