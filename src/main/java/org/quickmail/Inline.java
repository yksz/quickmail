package org.quickmail;

import java.io.InputStream;

public class Inline {
    private String mimeType;
    private String id;
    private String encoding;
    private InputStream inputStream;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        builder.append("Inline [mimeType=");
        builder.append(mimeType);
        builder.append(", id=");
        builder.append(id);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append(", inputStream=");
        builder.append(inputStream);
        builder.append("]");
        return builder.toString();
    }
}
