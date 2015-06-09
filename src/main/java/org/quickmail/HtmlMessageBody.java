package org.quickmail;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HtmlMessageBody {
    private static final String MIME_TYPE = "text/html";
    private static final String SUB_TYPE = "html";
    private String content;
    private Charset charset;
    private String encoding;
    private final List<Inline> inlines = new LinkedList<>();

    public static String getMimeType() {
        return MIME_TYPE;
    }

    public static String getSubType() {
        return SUB_TYPE;
    }

    public String getContent() {
        return content;
    }

    public HtmlMessageBody setContent(String content) {
        this.content = content;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public HtmlMessageBody setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public HtmlMessageBody setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public List<Inline> getInlines() {
        return inlines;
    }

    public HtmlMessageBody addInline(Inline... inlines) {
        return addInline(Arrays.asList(inlines));
    }

    public HtmlMessageBody addInline(List<Inline> inlines) {
        this.inlines.addAll(inlines);
        return this;
    }

    public boolean hasInline() {
        return !inlines.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HtmlMessageBody [content=");
        builder.append(content);
        builder.append(", charset=");
        builder.append(charset);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append(", inlines=");
        builder.append(inlines);
        builder.append("]");
        return builder.toString();
    }
}
