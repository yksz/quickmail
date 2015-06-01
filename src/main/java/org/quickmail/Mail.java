package org.quickmail;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Mail {
    private String fromAddress;
    private List<String> toAddresses = new LinkedList<>();
    private List<String> ccAddresses = new LinkedList<>();
    private List<String> bccAddresses = new LinkedList<>();
    private List<String> replyToAddresses = new LinkedList<>();
    private Date sentDate;
    private String subject;
    private String textMessage;
    private String htmlMessage;
    private List<Attachment> attachments = new LinkedList<>();

    public String getFrom() {
        return fromAddress;
    }

    public void setFrom(String address) {
        this.fromAddress = address;
    }

    public List<String> getTo() {
        return toAddresses;
    }

    public void addTo(String... addresses) {
        this.toAddresses.addAll(Arrays.asList(addresses));
    }

    public List<String> getCc() {
        return ccAddresses;
    }

    public void addCc(String... addresses) {
        this.ccAddresses.addAll(Arrays.asList(addresses));
    }

    public List<String> getBcc() {
        return bccAddresses;
    }

    public void addBcc(String... addresses) {
        this.bccAddresses.addAll(Arrays.asList(addresses));
    }

    public List<String> getReplyTo() {
        return replyToAddresses;
    }

    public void addReplyTo(String... addresses) {
        this.replyToAddresses.addAll(Arrays.asList(addresses));
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String message) {
        this.textMessage = message;
    }

    public String getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(String message) {
        this.htmlMessage = message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(Attachment... attachments) {
        this.attachments.addAll(Arrays.asList(attachments));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Mail [from=");
        builder.append(fromAddress);
        builder.append(", to=");
        builder.append(toAddresses);
        builder.append(", cc=");
        builder.append(ccAddresses);
        builder.append(", bcc=");
        builder.append(bccAddresses);
        builder.append(", replyTo=");
        builder.append(replyToAddresses);
        builder.append(", sentDate=");
        builder.append(sentDate);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", textMessage=");
        builder.append(textMessage);
        builder.append(", htmlMessage=");
        builder.append(htmlMessage);
        builder.append(", attachments=");
        builder.append(attachments);
        builder.append("]");
        return builder.toString();
    }
}
