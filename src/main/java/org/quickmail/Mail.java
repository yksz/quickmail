package org.quickmail;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Mail {
    private String fromAddr;
    private final List<String> toAddrs = new LinkedList<>();
    private final List<String> ccAddrs = new LinkedList<>();
    private final List<String> bccAddrs = new LinkedList<>();
    private final List<String> replyToAddrs = new LinkedList<>();
    private Date sentDate;
    private String subject;
    private Charset subjectCharset;
    private TextBody textBody;
    private HtmlBody htmlBody;
    private final List<Attachment> attachments = new LinkedList<>();

    public String getFrom() {
        return fromAddr;
    }

    public Mail setFrom(String address) {
        this.fromAddr = address;
        return this;
    }

    public List<String> getTo() {
        return toAddrs;
    }

    public Mail addTo(String... addresses) {
        return addTo(Arrays.asList(addresses));
    }

    public Mail addTo(List<String> addresses) {
        this.toAddrs.addAll(addresses);
        return this;
    }

    public Mail clearTo() {
        toAddrs.clear();
        return this;
    }

    public List<String> getCc() {
        return ccAddrs;
    }

    public Mail addCc(String... addresses) {
        return addCc(Arrays.asList(addresses));
    }

    public Mail addCc(List<String> addresses) {
        this.ccAddrs.addAll(addresses);
        return this;
    }

    public Mail clearCc() {
        ccAddrs.clear();
        return this;
    }

    public List<String> getBcc() {
        return bccAddrs;
    }

    public Mail addBcc(String... addresses) {
        return addBcc(Arrays.asList(addresses));
    }

    public Mail addBcc(List<String> addresses) {
        this.bccAddrs.addAll(addresses);
        return this;
    }

    public Mail clearBcc() {
        bccAddrs.clear();
        return this;
    }

    public List<String> getReplyTo() {
        return replyToAddrs;
    }

    public Mail addReplyTo(String... addresses) {
        return addReplyTo(Arrays.asList(addresses));
    }

    public Mail addReplyTo(List<String> addresses) {
        this.replyToAddrs.addAll(addresses);
        return this;
    }

    public Mail clearReplyTo() {
        replyToAddrs.clear();
        return this;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Mail setSentDate(Date sentDate) {
        this.sentDate = sentDate;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail setSubject(String subject, Charset charset) {
        this.subject = subject;
        this.subjectCharset = charset;
        return this;
    }

    public Charset getSubjectCharset() {
        return subjectCharset;
    }

    public Mail setSubjectCharset(Charset subjectCharset) {
        this.subjectCharset = subjectCharset;
        return this;
    }

    public TextBody getTextBody() {
        return textBody;
    }

    public Mail setTextBody(TextBody message) {
        this.textBody = message;
        return this;
    }

    public HtmlBody getHtmlBody() {
        return htmlBody;
    }

    public Mail setHtmlBody(HtmlBody message) {
        this.htmlBody = message;
        return this;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Mail addAttachment(Attachment... attachments) {
        return addAttachment(Arrays.asList(attachments));
    }

    public Mail addAttachment(List<Attachment> attachments) {
        this.attachments.addAll(attachments);
        return this;
    }

    public boolean hasTextMessage() {
        return textBody != null;
    }

    public boolean hasHtmlMessage() {
        return htmlBody != null;
    }

    public boolean hasAttachment() {
        return !attachments.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mail [from=");
        builder.append(fromAddr);
        builder.append(", to=");
        builder.append(toAddrs);
        builder.append(", cc=");
        builder.append(ccAddrs);
        builder.append(", bcc=");
        builder.append(bccAddrs);
        builder.append(", replyTo=");
        builder.append(replyToAddrs);
        builder.append(", sentDate=");
        builder.append(sentDate);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", subjectCharset=");
        builder.append(subjectCharset);
        builder.append(", textBody=");
        builder.append(textBody);
        builder.append(", htmlBody=");
        builder.append(htmlBody);
        builder.append(", attachments=");
        builder.append(attachments);
        builder.append("]");
        return builder.toString();
    }
}
