package org.quickmail.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;

public final class ParserSelector {
    private static final ParserSelector instance = new ParserSelector();
    private final Object lock = new Object();
    private MessageParser defaultParser;
    private final List<ParserSelectingRule> rules = new LinkedList<>();

    public static ParserSelector getInstance() {
        return instance;
    }

    private ParserSelector() {
    }

    public MessageParser selectParser(Message message) {
        Objects.requireNonNull(message, "message must not be null");
        synchronized (lock) {
            MessageParser parser;
            try {
                parser = selectParserByRules(message);
            } catch (MessagingException ignore) {
                return getDefaultParser();
            }
            return (parser != null) ? parser : getDefaultParser();
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
            if (headers == null) {
                return null;
            }
            for (String header : headers) {
                if (Pattern.compile(rule.getRegex()).matcher(header).find()) {
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

    public void addRule(ParserSelectingRule rule) {
        synchronized (lock) {
            rules.add(rule);
        }
    }

    public void removeRule(ParserSelectingRule rule) {
        synchronized (lock) {
            rules.remove(rule);
        }
    }

    public void clearRules() {
        synchronized (lock) {
            rules.clear();
        }
    }
}
