package org.quickmail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

public class Mail {
    private InternetAddress fromAddr;
    private final List<InternetAddress> toAddrs = new LinkedList<>();
    private final List<InternetAddress> ccAddrs = new LinkedList<>();
    private final List<InternetAddress> bccAddrs = new LinkedList<>();
    private final List<InternetAddress> replyToAddrs = new LinkedList<>();
    private Date sentDate;
    private String subject;
    private Charset subjectCharset;
    private TextBody textBody;
    private HtmlBody htmlBody;
    private final List<Attachment> attachments = new LinkedList<>();

    public InternetAddress getFrom() {
        return fromAddr;
    }

    public Mail setFrom(String address) throws AddressException {
        this.fromAddr = new InternetAddress(address);
        return this;
    }

    public Mail setFrom(String address, String personal)
            throws AddressException, UnsupportedEncodingException {
        this.fromAddr = new InternetAddress(address, personal);
        return this;
    }

    public Mail setFrom(String address, String personal, Charset charset)
            throws AddressException, UnsupportedEncodingException {
        this.fromAddr = new InternetAddress(address, personal, mimeCharset(charset));
        return this;
    }

    public Mail setFrom(InternetAddress address) {
        this.fromAddr = address;
        return this;
    }

    public List<InternetAddress> getTo() {
        return toAddrs;
    }

    public Mail addTo(String address) throws AddressException {
        this.toAddrs.add(new InternetAddress(address));
        return this;
    }

    public Mail addTo(String address, String personal)
            throws AddressException, UnsupportedEncodingException {
        this.toAddrs.add(new InternetAddress(address, personal));
        return this;
    }

    public Mail addTo(String address, String personal, Charset charset)
            throws AddressException, UnsupportedEncodingException {
        this.toAddrs.add(new InternetAddress(address, personal, mimeCharset(charset)));
        return this;
    }

    public Mail addTo(String... addresses) throws AddressException {
        for (String address : addresses) {
            addTo(address);
        }
        return this;
    }

    public Mail addTo(InternetAddress address) {
        this.toAddrs.add(address);
        return this;
    }

    public Mail addTo(InternetAddress... addresses) {
        return addTo(Arrays.asList(addresses));
    }

    public Mail addTo(List<InternetAddress> addresses) {
        this.toAddrs.addAll(addresses);
        return this;
    }

    public Mail clearTo() {
        this.toAddrs.clear();
        return this;
    }

    public List<InternetAddress> getCc() {
        return ccAddrs;
    }

    public Mail addCc(String address) throws AddressException {
        this.ccAddrs.add(new InternetAddress(address));
        return this;
    }

    public Mail addCc(String address, String personal)
            throws AddressException, UnsupportedEncodingException {
        this.ccAddrs.add(new InternetAddress(address, personal));
        return this;
    }

    public Mail addCc(String address, String personal, Charset charset)
            throws AddressException, UnsupportedEncodingException {
        this.ccAddrs.add(new InternetAddress(address, personal, mimeCharset(charset)));
        return this;
    }

    public Mail addCc(String... addresses) throws AddressException {
        for (String address : addresses) {
            addCc(address);
        }
        return this;
    }

    public Mail addCc(InternetAddress address) {
        this.ccAddrs.add(address);
        return this;
    }

    public Mail addCc(InternetAddress... addresses) {
        return addCc(Arrays.asList(addresses));
    }

    public Mail addCc(List<InternetAddress> addresses) {
        this.ccAddrs.addAll(addresses);
        return this;
    }

    public Mail clearCc() {
        this.ccAddrs.clear();
        return this;
    }

    public List<InternetAddress> getBcc() {
        return bccAddrs;
    }

    public Mail addBcc(String address) throws AddressException {
        this.bccAddrs.add(new InternetAddress(address));
        return this;
    }

    public Mail addBcc(String address, String personal)
            throws AddressException, UnsupportedEncodingException {
        this.bccAddrs.add(new InternetAddress(address, personal));
        return this;
    }

    public Mail addBcc(String address, String personal, Charset charset)
            throws AddressException, UnsupportedEncodingException {
        this.bccAddrs.add(new InternetAddress(address, personal, mimeCharset(charset)));
        return this;
    }

    public Mail addBcc(String... addresses) throws AddressException {
        for (String address : addresses) {
            addBcc(address);
        }
        return this;
    }

    public Mail addBcc(InternetAddress address) {
        this.bccAddrs.add(address);
        return this;
    }

    public Mail addBcc(InternetAddress... addresses) {
        return addBcc(Arrays.asList(addresses));
    }

    public Mail addBcc(List<InternetAddress> addresses) {
        this.bccAddrs.addAll(addresses);
        return this;
    }

    public Mail clearBcc() {
        this.bccAddrs.clear();
        return this;
    }

    public List<InternetAddress> getReplyTo() {
        return replyToAddrs;
    }

    public Mail addReplyTo(String address) throws AddressException {
        this.replyToAddrs.add(new InternetAddress(address));
        return this;
    }

    public Mail addReplyTo(String address, String personal)
            throws AddressException, UnsupportedEncodingException {
        this.replyToAddrs.add(new InternetAddress(address, personal));
        return this;
    }

    public Mail addReplyTo(String address, String personal, Charset charset)
            throws AddressException, UnsupportedEncodingException {
        this.replyToAddrs.add(new InternetAddress(address, personal, mimeCharset(charset)));
        return this;
    }

    public Mail addReplyTo(String... addresses) throws AddressException {
        for (String address : addresses) {
            addReplyTo(address);
        }
        return this;
    }

    public Mail addReplyTo(InternetAddress address) {
        this.replyToAddrs.add(address);
        return this;
    }

    public Mail addReplyTo(InternetAddress... addresses) {
        return addReplyTo(Arrays.asList(addresses));
    }

    public Mail addReplyTo(List<InternetAddress> addresses) {
        this.replyToAddrs.addAll(addresses);
        return this;
    }

    public Mail clearReplyTo() {
        this.replyToAddrs.clear();
        return this;
    }

    private String mimeCharset(Charset charset) {
        if (charset == null) {
            return null;
        }
        return MimeUtility.mimeCharset(charset.name());
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

    public Mail addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public Mail addAttachment(Attachment... attachments) {
        return addAttachment(Arrays.asList(attachments));
    }

    public Mail addAttachment(List<Attachment> attachments) {
        this.attachments.addAll(attachments);
        return this;
    }

    public Mail clearAttachment() {
        this.attachments.clear();
        return this;
    }

    public boolean hasTextBody() {
        return textBody != null;
    }

    public boolean hasHtmlBody() {
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
