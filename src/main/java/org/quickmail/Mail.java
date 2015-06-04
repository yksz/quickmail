package org.quickmail;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Mail {
    private String fromAddr;
    private List<String> toAddrs = new LinkedList<>();
    private List<String> ccAddrs = new LinkedList<>();
    private List<String> bccAddrs = new LinkedList<>();
    private List<String> replyToAddrs = new LinkedList<>();
    private Date sentDate;
    private String subject;
    private TextMessageBody textMsg;
    private HtmlMessageBody htmlMsg;
    private List<Attachment> attachments = new LinkedList<>();

    public String getFrom() {
        return fromAddr;
    }

    public void setFrom(String address) {
        this.fromAddr = address;
    }

    public List<String> getTo() {
        return toAddrs;
    }

    public void addTo(String... addresses) {
        addTo(Arrays.asList(addresses));
    }

    public void addTo(List<String> addresses) {
        this.toAddrs.addAll(addresses);
    }

    public List<String> getCc() {
        return ccAddrs;
    }

    public void addCc(String... addresses) {
        addCc(Arrays.asList(addresses));
    }

    public void addCc(List<String> addresses) {
        this.ccAddrs.addAll(addresses);
    }

    public List<String> getBcc() {
        return bccAddrs;
    }

    public void addBcc(String... addresses) {
        addBcc(Arrays.asList(addresses));
    }

    public void addBcc(List<String> addresses) {
        this.bccAddrs.addAll(addresses);
    }

    public List<String> getReplyTo() {
        return replyToAddrs;
    }

    public void addReplyTo(String... addresses) {
        addReplyTo(Arrays.asList(addresses));
    }

    public void addReplyTo(List<String> addresses) {
        this.replyToAddrs.addAll(addresses);
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

    public TextMessageBody getTextMessage() {
        return textMsg;
    }

    public void setTextMessage(TextMessageBody message) {
        this.textMsg = message;
    }

    public HtmlMessageBody getHtmlMessage() {
        return htmlMsg;
    }

    public void setHtmlMessage(HtmlMessageBody message) {
        this.htmlMsg = message;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(Attachment... attachments) {
        addAttachment(Arrays.asList(attachments));
    }

    public void addAttachment(List<Attachment> attachments) {
        this.attachments.addAll(attachments);
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
        builder.append(", textMessage=");
        builder.append(textMsg);
        builder.append(", htmlMessage=");
        builder.append(htmlMsg);
        builder.append(", attachments=");
        builder.append(attachments);
        builder.append("]");
        return builder.toString();
    }
}
