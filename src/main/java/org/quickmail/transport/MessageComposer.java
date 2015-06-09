package org.quickmail.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.quickmail.Attachment;
import org.quickmail.HtmlMessageBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextMessageBody;

class MessageComposer {
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    private MimeMessage msg;

    public MessageComposer(Session session) {
        this.msg = new MimeMessage(Objects.requireNonNull(session, "session must not be null"));
    }

    public Message compose(Mail mail) throws MessagingException, IOException {
        Objects.requireNonNull(mail, "mail must not be null");
        if (mail.getFrom() != null) {
            msg.setFrom(new InternetAddress(mail.getFrom()));
        }
        if (mail.getTo() != null) {
            msg.setRecipients(RecipientType.TO, convertToAddresses(mail.getTo()));
        }
        if (mail.getCc() != null) {
            msg.setRecipients(RecipientType.CC, convertToAddresses(mail.getCc()));
        }
        if (mail.getBcc() != null) {
            msg.setRecipients(RecipientType.BCC, convertToAddresses(mail.getBcc()));
        }
        if (mail.getReplyTo() != null) {
            msg.setReplyTo(convertToAddresses(mail.getReplyTo()));
        }
        msg.setSentDate((mail.getSentDate() != null) ? mail.getSentDate() : new Date());
        msg.setSubject(mail.getSubject(), mail.getSubjectCharset().name());
        setMessageContent(mail);
        return msg;
    }

    private Address[] convertToAddresses(List<String> strs) throws AddressException {
        return strs.stream()
                .map(str -> {
                    try {
                        return new InternetAddress(str);
                    } catch (AddressException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Address[]::new);
    }

    private void setMessageContent(Mail mail) throws MessagingException, IOException {
        if (mail.hasAttachment()) {
            msg.setContent(composeMultipartMixed(mail));
        } else if (mail.hasTextMessage() && mail.hasHtmlMessage()) {
            msg.setContent(composeMultipartAlternative(mail));
        } else if (mail.hasHtmlMessage() && mail.getHtmlMessage().hasInline()){
            msg.setContent(composeMultipartRelated(mail));
        } else if (mail.hasTextMessage()) {
            TextMessageBody textMsg = mail.getTextMessage();
            msg.setText(textMsg.getContent(), getMimeCharset(textMsg.getCharset()), TextMessageBody.getSubType());
            if (textMsg.getEncoding() != null) {
                msg.setHeader("Content-Transfer-Encoding", textMsg.getEncoding());
            }
        } else if (mail.hasHtmlMessage()) {
            HtmlMessageBody htmlMsg = mail.getHtmlMessage();
            msg.setText(htmlMsg.getContent(), getMimeCharset(htmlMsg.getCharset()), HtmlMessageBody.getSubType());
            if (htmlMsg.getEncoding() != null) {
                msg.setHeader("Content-Transfer-Encoding", htmlMsg.getEncoding());
            }
        } else { // empty message
            msg.setText("", TextMessageBody.getSubType());
        }
    }

    private String getMimeCharset(Charset charset) {
        if (charset == null) {
            return null;
        }
        return MimeUtility.mimeCharset(charset.name());
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
    private Multipart composeMultipartMixed(Mail mail) throws MessagingException, IOException {
        Multipart mixed = new MimeMultipart("mixed");
        if (mail.hasTextMessage() && mail.hasHtmlMessage()) {
            addMultipart(mixed, composeMultipartAlternative(mail));
        } else if (mail.hasHtmlMessage() && mail.getHtmlMessage().hasInline()) {
            addMultipart(mixed, composeMultipartRelated(mail));
        } else if (mail.hasTextMessage()) {
            mixed.addBodyPart(composeTextMessage(mail));
        } else if (mail.hasHtmlMessage()) {
            mixed.addBodyPart(composeHtmlMessage(mail));
        }
        for (Attachment attachment : mail.getAttachments()) {
            mixed.addBodyPart(composeAttachment(attachment));
        }
        return mixed;
    }

    private Multipart composeMultipartAlternative(Mail mail) throws MessagingException, IOException {
        Multipart alternative = new MimeMultipart("alternative");
        alternative.addBodyPart(composeTextMessage(mail));
        if (mail.getHtmlMessage().hasInline()) {
            addMultipart(alternative, composeMultipartRelated(mail));
        } else {
            alternative.addBodyPart(composeHtmlMessage(mail));
        }
        return alternative;
    }

    private Multipart composeMultipartRelated(Mail mail) throws MessagingException, IOException {
        Multipart related = new MimeMultipart("related");
        related.addBodyPart(composeHtmlMessage(mail));
        for (Inline inline : mail.getHtmlMessage().getInlines()) {
            related.addBodyPart(composeInline(inline));
        }
        return related;
    }

    private void addMultipart(Multipart dst, Multipart src) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(src);
        dst.addBodyPart(bodyPart);
    }

    private BodyPart composeTextMessage(Mail mail) throws MessagingException {
        TextMessageBody textMsg = mail.getTextMessage();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(textMsg.getContent(), getMimeCharset(textMsg.getCharset()), TextMessageBody.getSubType());
        if (textMsg.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", textMsg.getEncoding());
        }
        return bodyPart;
    }

    private BodyPart composeHtmlMessage(Mail mail) throws MessagingException {
        HtmlMessageBody htmlMsg = mail.getHtmlMessage();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(htmlMsg.getContent(), getMimeCharset(htmlMsg.getCharset()), HtmlMessageBody.getSubType());
        if (htmlMsg.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", htmlMsg.getEncoding());
        }
        return bodyPart;
    }

    private BodyPart composeAttachment(Attachment attachment) throws MessagingException, IOException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDisposition("attachment");
        String mimeCharset = getMimeCharset(attachment.getCharset());
        if (attachment.getFile() != null) {
            bodyPart.attachFile(attachment.getFile());
            setContentType(bodyPart, attachment.getMimeType(), mimeCharset);
        } else {
            String mimeType = (attachment.getMimeType() != null) ? attachment.getMimeType() : DEFAULT_MIME_TYPE;
            DataSource dataSource = new ByteArrayDataSource(attachment.getInputStream(), mimeType);
            bodyPart.setDataHandler(new DataHandler(dataSource));
            setContentType(bodyPart, mimeType, mimeCharset);
        }
        if (attachment.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", attachment.getEncoding());
        }
        try {
            String filename = MimeUtility.encodeWord(attachment.getName(), mimeCharset, "B");
            bodyPart.setFileName(filename);
        } catch (UnsupportedEncodingException e) {
            bodyPart.setFileName(attachment.getName());
        }
        return bodyPart;
    }

    private MimeBodyPart composeInline(Inline inline) throws MessagingException, IOException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDisposition("inline");
        if (inline.getFile() != null) {
            bodyPart.attachFile(inline.getFile());
            setContentType(bodyPart, inline.getMimeType(), null);
        } else {
            String mimeType = (inline.getMimeType() != null) ? inline.getMimeType() : DEFAULT_MIME_TYPE;
            DataSource dataSource = new ByteArrayDataSource(inline.getInputStream(), mimeType);
            bodyPart.setDataHandler(new DataHandler(dataSource));
        }
        if (inline.getId() != null) {
            bodyPart.setHeader("Content-Id", inline.getId());
        }
        if (inline.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", inline.getEncoding());
        }
        return bodyPart;
    }

    private void setContentType(BodyPart bodyPart, String mimeType, String mimeCharset) throws MessagingException {
        if (mimeType == null) {
            return;
        }
        if (mimeCharset != null) {
            bodyPart.setHeader("Content-Type", mimeType + "; charset=" + mimeCharset);
        } else {
            bodyPart.setHeader("Content-Type", mimeType);
        }
    }
}
