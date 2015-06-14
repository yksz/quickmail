package org.quickmail;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HtmlBody {
    private static final String MIME_TYPE = "text/html";
    private static final String SUB_TYPE = "html";
    private final String content;
    private Charset charset;
    private String encoding;
    private final List<Inline> inlines = new LinkedList<>();

    public static String getMimeType() {
        return MIME_TYPE;
    }

    public static String getSubType() {
        return SUB_TYPE;
    }

    public HtmlBody(String content) {
        this.content = Objects.requireNonNull(content, "content must not be null");
    }

    public String getContent() {
        return content;
    }

    public Charset getCharset() {
        return charset;
    }

    public HtmlBody setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public HtmlBody setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public List<Inline> getInlines() {
        return inlines;
    }

    public HtmlBody addInline(Inline inline) {
        this.inlines.add(inline);
        return this;
    }

    public HtmlBody addInlines(Inline... inlines) {
        return addInlines(Arrays.asList(inlines));
    }

    public HtmlBody addInlines(List<Inline> inlines) {
        this.inlines.addAll(inlines);
        return this;
    }

    public HtmlBody clearInlines() {
        this.inlines.clear();
        return this;
    }

    public boolean hasInline() {
        return !inlines.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HtmlBody [content=");
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
