package org.quickmail.parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.quickmail.HtmlBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextBody;

public class DefaultMessageParser implements MessageParser {
    @Override
    public Mail parse(Message msg) throws MessageParseException {
        Objects.requireNonNull(msg, "msg must not be null");
        Mail mail = new Mail();
        mail.setFrom(parseFrom(msg));
        mail.addTo(parseTo(msg));
        mail.addCc(parseCc(msg));
        mail.addBcc(parseBcc(msg));
        mail.addReplyTo(parseReplyTo(msg));
        mail.setSentDate(parseSentDate(msg));
        mail.setSubject(parseSubject(msg));
        mail.setSubjectCharset(parseSubjectCharset(msg));
        MessageContent msgContent = parseMessageContent(msg);
        mail.setTextBody(msgContent.getTextBody());
        mail.setHtmlBody(msgContent.getHtmlBody());
        mail.addAttachments(msgContent.getAttachments());
        return mail;
    }

    protected InternetAddress parseFrom(Message msg) throws MessageParseException {
        InternetAddress[] from;
        try {
            from = (InternetAddress[]) msg.getFrom();
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse From", e);
        }
        if (from == null || from.length == 0) {
            return null;
        }
        return from[0];
    }

    protected InternetAddress[] parseTo(Message msg) throws MessageParseException {
        try {
            return parseRecipients(msg, RecipientType.TO);
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse To", e);
        }
    }

    protected InternetAddress[] parseCc(Message msg) throws MessageParseException {
        try {
            return parseRecipients(msg, RecipientType.CC);
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse Cc", e);
        }
    }

    protected InternetAddress[] parseBcc(Message msg) throws MessageParseException {
        try {
            return parseRecipients(msg, RecipientType.BCC);
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse Bcc", e);
        }
    }

    private InternetAddress[] parseRecipients(Message msg, RecipientType type) throws MessagingException {
        InternetAddress[] addrs = (InternetAddress[]) msg.getRecipients(type);
        return (addrs != null) ? addrs : new InternetAddress[0];
    }

    protected InternetAddress[] parseReplyTo(Message msg) throws MessageParseException {
        InternetAddress[] addrs;
        try {
            addrs = (InternetAddress[]) msg.getReplyTo();
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse Reply-To");
        }
        return (addrs != null) ? addrs : new InternetAddress[0];
    }

    protected Date parseSentDate(Message msg) throws MessageParseException {
        try {
            return msg.getSentDate();
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse SentDate", e);
        }
    }

    protected String parseSubject(Message msg) throws MessageParseException {
        try {
            return msg.getSubject();
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse Subject", e);
        }
    }

    protected Charset parseSubjectCharset(Message msg) throws MessageParseException {
        String[] subject;
        try {
            subject = msg.getHeader("Subject");
        } catch (MessagingException e) {
            throw new MessageParseException("Failed to parse SubjectCharset", e);
        }
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

    protected MessageContent parseMessageContent(Message msg) throws MessageParseException {
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
                ContentType contentType = new ContentType(msg.getContentType());
                throw new MessageParseException("Unknown content-type: " + contentType.getBaseType());
            }
        } catch (Exception e) {
            throw new MessageParseException("Failed to parse MessageContent", e);
        }
    }

    private void parseTextpart(MimePart part, String content, MessageContent msgContent) throws MessagingException {
        if (part.isMimeType("text/plain")) {
            ContentType contentType = new ContentType(part.getContentType());
            TextBody textBody = new TextBody(content);
            textBody.setCharset(getCharset(contentType));
            textBody.setEncoding(part.getEncoding());
            msgContent.setTextBody(textBody);
        } else if (part.isMimeType("text/html")) {
            ContentType contentType = new ContentType(part.getContentType());
            HtmlBody htmlBody = new HtmlBody(content);
            htmlBody.setCharset(getCharset(contentType));
            htmlBody.setEncoding(part.getEncoding());
            msgContent.setHtmlBody(htmlBody);
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
        ContentType contentType = new ContentType(multipart.getContentType());
        String subType = contentType.getSubType();
        if (subType.equalsIgnoreCase("mixed")) {
            parseMultipartMixed(multipart, msgContent);
        } else if (subType.equalsIgnoreCase("alternative")) {
            parseMultipartAlternative(multipart, msgContent);
        } else if (subType.equalsIgnoreCase("related")) {
            parseMultipartRelated(multipart, msgContent);
        } else {
            throw new MessageParseException("Not support this multipart: " + contentType.getBaseType());
        }
    }

    private void parseMultipartMixed(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart part = (MimePart) multipart.getBodyPart(i);
            String disp = part.getDisposition();
            if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) { // attachment
                msgContent.addAttachment(createAttachment(part));
            } else {
                Object content = part.getContent();
                if (content instanceof Multipart) {
                    parseMultipart((Multipart) content, msgContent);
                } else if (content instanceof String) {
                    parseTextpart(part, (String) content, msgContent);
                } else { // attachment
                    msgContent.addAttachment(createAttachment(part));
                }
            }
        }
    }

    private void parseMultipartAlternative(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart part = (MimePart) multipart.getBodyPart(i);
            Object content = part.getContent();
            if (content instanceof Multipart) {
                parseMultipart((Multipart) content, msgContent);
            } else if (content instanceof String) {
                parseTextpart(part, (String) content, msgContent);
            } else {
                ContentType contentType = new ContentType(part.getContentType());
                throw new MessageParseException("Unknown content-type at multipart/alternative: "
                        + contentType.getBaseType());
            }
        }
    }

    private void parseMultipartRelated(Multipart multipart, MessageContent msgContent)
            throws MessagingException, IOException {
        List<Inline> inlines= new LinkedList<>();
        for (int i = 0; i < multipart.getCount(); i++) {
            MimePart part = (MimePart) multipart.getBodyPart(i);
            String disp = part.getDisposition();
            if (disp != null && disp.equalsIgnoreCase(Part.INLINE)) { // inline
                inlines.add(createInline(part));
            } else {
                Object content = part.getContent();
                if (content instanceof Multipart) {
                    parseMultipart((Multipart) content, msgContent);
                } else if (content instanceof String) {
                    parseTextpart(part, (String) content, msgContent);
                } else { // inline
                    inlines.add(createInline(part));
                }
            }
        }
        if (msgContent.getHtmlBody() != null) {
            msgContent.getHtmlBody().addInlines(inlines);
        }
    }

    private Attachment createAttachment(MimePart part) throws MessagingException, IOException {
        String encodedFileName = part.getFileName();
        String decodedFileName = null;
        if (encodedFileName != null) {
            decodedFileName = MimeUtility.decodeText(encodedFileName);
        }
        ContentType contentType = new ContentType(part.getContentType());
        Attachment attachment = new Attachment(part.getInputStream());
        attachment.setMimeType(contentType.getBaseType());
        attachment.setCharset(getCharset(contentType));
        attachment.setName(decodedFileName, getCharset(encodedFileName));
        attachment.setEncoding(part.getEncoding());
        return attachment;
    }

    private Inline createInline(MimePart part) throws MessagingException, IOException {
        ContentType contentType = new ContentType(part.getContentType());
        Inline inline = new Inline(part.getInputStream());
        inline.setMimeType(contentType.getBaseType());
        inline.setId(getCid(part.getContentID()));
        inline.setEncoding(part.getEncoding());
        return inline;
    }

    private String getCid(String contentId) {
        Pattern p = Pattern.compile("<(.+)>");
        Matcher m = p.matcher(contentId);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
