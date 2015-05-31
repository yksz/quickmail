package org.quickmail.parser;

/**
 * This class is the rule to select a suitable {@link MessageParser} for the message.
 *
 * <pre>
 * Example1
 *   Header: User-Agent
 *   Regex: Thunderbird
 *   MessageParser: org.example.ThunderbirdParser
 *
 * Example2
 *   Header: From
 *   Regex: gmail.com
 *   MessageParser: org.example.GmailParser
 * </pre>
 */
public class ParserSelectingRule {
    private final String header;
    private final String regex;
    private final MessageParser parser;

    public ParserSelectingRule(String header, String regex, MessageParser parser) {
        this.header = header;
        this.regex = regex;
        this.parser = parser;
    }

    public String getHeader() {
        return header;
    }

    public String getRegex() {
        return regex;
    }

    public MessageParser getParser() {
        return parser;
    }
}