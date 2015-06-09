package org.quickmail.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.quickmail.Attachment;
import org.quickmail.HtmlMessageBody;
import org.quickmail.TextMessageBody;

public class MessageContent {
    private TextMessageBody textMessage;
    private HtmlMessageBody htmlMessage;
    private List<Attachment> attachments = new LinkedList<>();

    public TextMessageBody getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessageBody textMessage) {
        this.textMessage = textMessage;
    }

    public HtmlMessageBody getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(HtmlMessageBody htmlMessage) {
        this.htmlMessage = htmlMessage;
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
