package org.quickmail.parser;

import java.util.List;
import java.util.Objects;

import javax.mail.Message;
import javax.mail.MessagingException;

public final class ParserSelector {
    private static final ParserSelector instance = new ParserSelector();
    private final Object lock = new Object();
    private MessageParser defaultParser;
    private List<ParserSelectingRule> rules;

    public static ParserSelector getInstance() {
        return instance;
    }

    private ParserSelector() {
    }

    public MessageParser selectParser(Message message) throws MessagingException {
        Objects.requireNonNull(message, "message must not be null");
        synchronized (lock) {
            MessageParser parser = selectParserByRules(message);
            if (parser != null) {
                return parser;
            }
            return getDefaultParser();
        }
    }

    private MessageParser getDefaultParser() {
        if (defaultParser == null) {
            defaultParser = new DefaultMessageParser();
        }
        return defaultParser;
    }

    private MessageParser selectParserByRules(Message message) throws MessagingException {
        for (ParserSelectingRule rule : rules) {
            String[] headers = message.getHeader(rule.getHeader());
            for (String header : headers) {
                if (header.matches(rule.getRegex())) {
                    return rule.getParser();
                }
            }
        }
        return null;
    }

    public void setDefaultParser(MessageParser parser) {
        synchronized (lock) {
            this.defaultParser = parser;
        }
    }

    public void addParserSelectingRule(ParserSelectingRule rule) {
        synchronized (lock) {
            rules.add(rule);
        }
    }
}