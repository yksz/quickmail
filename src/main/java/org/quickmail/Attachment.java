package org.quickmail;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

public class Attachment {
    private final File file;
    private final InputStream inputStream;
    private String mimeType;
    private Charset charset;
    private String name;
    private Charset nameCharset;
    private String encoding;

    public Attachment(File file) {
        this.file = Objects.requireNonNull(file, "file must not be null");
        this.inputStream = null;
    }

    public Attachment(InputStream inputStream) {
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

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String name, Charset charset) {
        this.name = name;
        this.nameCharset = charset;
    }

    public Charset getNameCharset() {
        return nameCharset;
    }

    public void setNameCharset(Charset nameCharset) {
        this.nameCharset = nameCharset;
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
        builder.append("Attachment [file=");
        builder.append(file);
        builder.append(", inputStream=");
        builder.append(inputStream);
        builder.append(", mimeType=");
        builder.append(mimeType);
        builder.append(", charset=");
        builder.append(charset);
        builder.append(", name=");
        builder.append(name);
        builder.append(", nameCharset=");
        builder.append(nameCharset);
        builder.append(", encoding=");
        builder.append(encoding);
        builder.append("]");
        return builder.toString();
    }
}
