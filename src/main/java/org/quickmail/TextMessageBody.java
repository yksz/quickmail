package org.quickmail;

import java.nio.charset.Charset;
import java.util.Objects;

public class TextMessageBody {
    private static final String MIME_TYPE = "text/plain";
    private static final String SUB_TYPE = "plain";
    private final String content;
    private Charset charset;
    private String encoding;

    public static String getMimeType() {
        return MIME_TYPE;
    }

    public static String getSubType() {
        return SUB_TYPE;
    }

    public TextMessageBody(String content) {
        this.content = Objects.requireNonNull(content, "content must not be null");
    }

    public String getContent() {
        return content;
    }

    public Charset getCharset() {
        return charset;
    }

    public TextMessageBody setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public TextMessageBody setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TextMessageBody [content=");
        builder.append(content);
        builder.append(", charset=");
        builder.append(charset);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append("]");
        return builder.toString();
    }
}
