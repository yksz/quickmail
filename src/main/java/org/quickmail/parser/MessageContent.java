package org.quickmail.parser;

import java.util.LinkedList;
import java.util.List;

import org.quickmail.Attachment;
import org.quickmail.HtmlMessageBody;
import org.quickmail.TextMessageBody;

public class MessageContent {
    private TextMessageBody textMessage = new TextMessageBody();
    private HtmlMessageBody htmlMessage = new HtmlMessageBody();
    private List<Attachment> attachments = new LinkedList<>();

    public TextMessageBody getTextMessage() {
        return textMessage;
    }

    public HtmlMessageBody getHtmlMessage() {
        return htmlMessage;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
