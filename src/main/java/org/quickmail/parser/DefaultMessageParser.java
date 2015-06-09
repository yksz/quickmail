package org.quickmail.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;

import org.quickmail.Attachment;
import org.quickmail.HtmlMessageBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextMessageBody;

public class DefaultMessageParser implements MessageParser {
    @Override
    public Mail parse(Message message) throws MessagingException {
        Objects.requireNonNull(message, "message must not be null");
        Mail mail = new Mail();
        mail.setFrom(parseFrom(message));
        mail.addTo(parseTo(message));
        mail.addCc(parseCc(message));
        mail.addBcc(parseBcc(message));
        mail.addReplyTo(parseReplyTo(message));
        mail.setSentDate(parseSentDate(message));
        mail.setSubject(parseSubject(message));
        mail.setSubjectCharset(parseSubjectCharset(message));
        MessageContent messageContent = parseMessageContent(message);
        mail.setTextMessage(messageContent.getTextMessage());
        mail.setHtmlMessage(messageContent.getHtmlMessage());
        mail.addAttachment(messageContent.getAttachments());
        return mail;
    }

    protected String parseFrom(Message msg) throws MessagingException {
        InternetAddress[] from = (InternetAddress[]) msg.getFrom();
        return (from == null) ? null : from[0].getAddress();
    }

    protected String[] parseTo(Message msg) throws MessagingException {
        return parseRecipients(msg, RecipientType.TO);
    }

    protected String[] parseCc(Message msg) throws MessagingException {
        return parseRecipients(msg, RecipientType.CC);
    }

    protected String[] parseBcc(Message msg) throws MessagingException {
        return parseRecipients(msg, RecipientType.BCC);
    }

    private String[] parseRecipients(Message msg, RecipientType type) throws MessagingException {
        InternetAddress[] addrs = (InternetAddress[]) msg.getRecipients(type);
        if (addrs == null) {
            return new String[0];
        }
        return Stream.of(addrs)
                .map(addr -> addr.getAddress())
                .toArray(String[]::new);
    }

    protected String[] parseReplyTo(Message msg) throws MessagingException {
        InternetAddress[] addrs = (InternetAddress[]) msg.getReplyTo();
        if (addrs == null) {
            return new String[0];
        }
        return Stream.of(addrs)
                .map(addr -> addr.getAddress())
                .toArray(String[]::new);
    }

    protected Date parseSentDate(Message msg) throws MessagingException {
        return msg.getSentDate();
    }

    protected String parseSubject(Message msg) throws MessagingException {
        return msg.getSubject();
    }

    protected Charset parseSubjectCharset(Message msg) throws MessagingException {
        String[] subject = msg.getHeader("Subject");
        if (subject == null || subject.length == 0) {
            return null;
        }
        return getCharset(subject[0]);
    }

    /**
     * Get charset from "=?charset?encoding?encoded-text?="
     *
     * @param encodedWord
     * @return
     */
    private Charset getCharset(String encodedWord) {
        if (encodedWord == null) {
            return null;
        }
        Pattern p = Pattern.compile("^=\\?([\\w\\-]+?)\\?");
        Matcher m = p.matcher(encodedWord);
        if (m.find()) {
            return Charset.forName(MimeUtility.javaCharset(m.group(1)));
        }
        return null;
    }

    protected MessageContent parseMessageContent(Message msg) throws MessagingException {
        try {
            Object content = msg.getContent();
            MessageContent msgContent = new MessageContent();
            if (content instanceof Multipart) { // Content-Type: multipart/xxxx
                parseMultipart((Multipart) content, msgContent);
                return msgContent;
            } else if (content instanceof String) { // Content-Type: text/plain or text/html
                parseTextpart((MimePart) msg, (String) content, msgContent);
                return msgContent;
            } else {
                throw new MessagingException("Unknown content-type at root: " + msg.getContentType());
            }
        } catch (IOException e) {
            throw new MessagingException("", e);
        }
    }

    private void parseTextpart(MimePart mimePart, String content, MessageContent msgContent) throws MessagingException {
        if (mimePart.isMimeType("text/plain")) {
            ContentType contentType = new ContentType(mimePart.getContentType());
            TextMessageBody textMsg = new TextMessageBody();
            textMsg.setContent(content);
            textMsg.setCharset(getCharset(contentType));
            textMsg.setEncoding(mimePart.getEncoding());
            msgContent.setTextMessage(textMsg);
        } else if (mimePart.isMimeType("text/html")) {
            ContentType contentType = new ContentType(mimePart.getContentType());
            HtmlMessageBody htmlMsg = new HtmlMessageBody();
            htmlMsg.setContent(content);
            htmlMsg.setCharset(getCharset(contentType));
            htmlMsg.setEncoding(mimePart.getEncoding());
            msgContent.setHtmlMessage(htmlMsg);
        }
    }

    private Charset getCharset(ContentType contentType) {
        String charset = contentType.getParameter("charset");
        if (charset == null) {
            return null;
        }
        return Charset.forName(MimeUtility.javaCharset(charset));
    }

    /**
     * <pre>
     *   multipart/mixed
     *   |
     *   |--mulpart/alternative
     *   |  |
     *   |  |--text/plain
     *   |  |
     *   |  |--multipart/related
     *   |     |
     *   |     |--text/html
     *   |     |
     *   |     |--image/xxxx (inline)
     *   |
     *   |--application/xxxx (attachment)
     * </pre>
     */
    private void parseMultipart(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        MimePart mimePart = (MimePart) multipart.getParent();
        if (mimePart.isMimeType("multipart/mixed")) {
            parseMultipartMixed(multipart, msgContent);
        } else if (mimePart.isMimeType("multipart/alternative")) {
            parseMultipartAlternative(multipart, msgContent);
        } else if (mimePart.isMimeType("multipart/related")) {
            parseMultipartRelated(multipart, msgContent);
        } else {
            ContentType contentType = new ContentType(mimePart.getContentType());
            throw new MessagingException("Unknown multipart: " + contentType.getBaseType());
        }
    }

    private void parseMultipartMixed(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart mimePart = (MimePart) multipart.getBodyPart(i);
            String disp = mimePart.getDisposition();
            if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) { // attachment
                msgContent.addAttachment(createAttachment(mimePart));
            } else {
                Object content = mimePart.getContent();
                if (content instanceof Multipart) {
                    parseMultipart((Multipart) content, msgContent);
                } else if (content instanceof String) {
                    parseTextpart(mimePart, (String) content, msgContent);
                } else {
                    msgContent.addAttachment(createAttachment(mimePart));
                }
            }
        }
    }

    private void parseMultipartAlternative(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart mimePart = (MimePart) multipart.getBodyPart(i);
            Object content = mimePart.getContent();
            if (content instanceof Multipart) {
                parseMultipart((Multipart) content, msgContent);
            } else if (content instanceof String) {
                parseTextpart(mimePart, (String) content, msgContent);
            } else {
                ContentType contentType = new ContentType(mimePart.getContentType());
                throw new MessagingException("Unknown content-type at multipart/alternative: " + contentType.getBaseType());
            }
        }
    }

    private void parseMultipartRelated(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        List<Inline> inlines= new LinkedList<>();
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart mimePart = (MimePart) multipart.getBodyPart(i);
            Object content = mimePart.getContent();
            if (content instanceof Multipart) {
                parseMultipart((Multipart) content, msgContent);
            } else if (content instanceof String) {
                parseTextpart(mimePart, (String) content, msgContent);
            } else { // inline
                inlines.add(createInline(mimePart));
            }
        }
        if (msgContent.getHtmlMessage() != null) {
            msgContent.getHtmlMessage().addInline(inlines);
        }
    }

    private Attachment createAttachment(MimePart mimePart) throws MessagingException, IOException {
        String encodedFileName = mimePart.getFileName();
        String decodedFileName = null;
        if (encodedFileName != null) {
            decodedFileName = MimeUtility.decodeText(encodedFileName);
        }
        ContentType contentType = new ContentType(mimePart.getContentType());
        Attachment attachment = new Attachment(mimePart.getInputStream());
        attachment.setMimeType(contentType.getBaseType());
        attachment.setCharset(getCharset(contentType));
        attachment.setName(decodedFileName, getCharset(encodedFileName));
        attachment.setEncoding(mimePart.getEncoding());
        return attachment;
    }

    private Inline createInline(MimePart mimePart) throws MessagingException, IOException {
        ContentType contentType = new ContentType(mimePart.getContentType());
        Inline inline = new Inline(mimePart.getInputStream());
        inline.setMimeType(contentType.getBaseType());
        inline.setId(mimePart.getContentID());
        inline.setEncoding(mimePart.getEncoding());
        return inline;
    }
}
