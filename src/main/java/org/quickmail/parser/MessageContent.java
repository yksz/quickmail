package org.quickmail.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.quickmail.Attachment;
import org.quickmail.HtmlBody;
import org.quickmail.TextBody;

public class MessageContent {
    private TextBody textBody;
    private HtmlBody htmlBody;
    private List<Attachment> attachments = new LinkedList<>();

    public TextBody getTextBody() {
        return textBody;
    }

    public void setTextBody(TextBody textBody) {
        this.textBody = textBody;
    }

    public HtmlBody getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(HtmlBody htmlBody) {
        this.htmlBody = htmlBody;
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
}
