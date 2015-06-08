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

    public void setContent(String content) {
        this.content = content;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public List<Inline> getInlines() {
        return inlines;
    }

    public void addInline(Inline... inlines) {
        addInline(Arrays.asList(inlines));
    }

    public void addInline(List<Inline> inlines) {
        this.inlines.addAll(inlines);
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
