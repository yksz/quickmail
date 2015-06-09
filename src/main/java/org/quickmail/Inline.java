package org.quickmail;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

public class Inline {
    private final File file;
    private final InputStream inputStream;
    private String mimeType;
    private String id;
    private String encoding;

    public Inline(File file) {
        this.file = Objects.requireNonNull(file, "file must not be null");
        this.inputStream = null;
    }

    public Inline(InputStream inputStream) {
        this.inputStream = Objects.requireNonNull(inputStream, "inputStream must not be null");
        this.file = null;
    }

    public File getFile() {
        return file;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Inline setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getId() {
        return id;
    }

    public Inline setId(String id) {
        this.id = id;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public Inline setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Inline [file=");
        builder.append(file);
        builder.append(", inputStream=");
        builder.append(inputStream);
        builder.append(", mimeType=");
        builder.append(mimeType);
        builder.append(", id=");
        builder.append(id);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append("]");
        return builder.toString();
    }
}
