package org.quickmail;

import java.io.InputStream;

public class Attachment {
    private String mimeType;
    private String name;
    private String charset;
    private String encoding;
    private InputStream inputStream;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Attachment [mimeType=");
        builder.append(mimeType);
        builder.append(", name=");
        builder.append(name);
        builder.append(", charset=");
        builder.append(charset);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append(", inputStream=");
        builder.append(inputStream);
        builder.append("]");
        return builder.toString();
    }
}
