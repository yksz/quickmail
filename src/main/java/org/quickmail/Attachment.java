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

    public Attachment setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public Attachment setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String getName() {
        return name;
    }

    public Attachment setName(String name) {
        this.name = name;
        return this;
    }

    public Attachment setName(String name, Charset charset) {
        this.name = name;
        this.nameCharset = charset;
        return this;
    }

    public Charset getNameCharset() {
        return nameCharset;
    }

    public Attachment setNameCharset(Charset nameCharset) {
        this.nameCharset = nameCharset;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public Attachment setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
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
