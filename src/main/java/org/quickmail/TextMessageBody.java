package org.quickmail;

public class TextMessageBody {
    private static final String MIME_TYPE = "text/plain";
    private final String content;
    private String charset;
    private String encoding = "7bit";

    public TextMessageBody(String content) {
        this.content = content;
    }

    public static String getMimeType() {
        return MIME_TYPE;
    }

    public String getContent() {
        return content;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
