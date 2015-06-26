package org.quickmail.composer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.quickmail.Attachment;
import org.quickmail.HtmlBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextBody;

public class MessageComposer {
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private static final String DEFAULT_ATTACHMENT_ENCODING = "base64";
    private static final String DEFAULT_INLINE_ENCODING = "base64";

    private final Session session;

    public MessageComposer(Session session) {
        this.session = Objects.requireNonNull(session, "session must not be null");
    }

    public Message compose(Mail mail) throws MessagingException, IOException {
        Objects.requireNonNull(mail, "mail must not be null");
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(mail.getFrom());
        msg.setRecipients(RecipientType.TO, toInternetAddressArray(mail.getTo()));
        msg.setRecipients(RecipientType.CC, toInternetAddressArray(mail.getCc()));
        msg.setRecipients(RecipientType.BCC, toInternetAddressArray(mail.getBcc()));
        msg.setReplyTo(toInternetAddressArray(mail.getReplyTo()));
        msg.setSentDate(getSentDate(mail));
        msg.setSubject(mail.getSubject(), mimeCharset(mail.getSubjectCharset()));
        setMessageContent(mail, msg);
        for (Header header : mail.getHeaders()) {
            msg.addHeader(header.getName(), header.getValue());
        }
        msg.saveChanges();
        return msg;
    }

    private InternetAddress[] toInternetAddressArray(List<InternetAddress> list) {
        return list.toArray(new InternetAddress[0]);
    }

    private Date getSentDate(Mail mail) {
        if (mail.getSentDate() == null) {
            return new Date();
        }
        return mail.getSentDate();
    }

    private void setMessageContent(Mail mail, MimeMessage msg) throws MessagingException, IOException {
        if (mail.hasAttachment()) {
            msg.setContent(composeMultipartMixed(mail));
        } else if (mail.hasTextBody() && mail.hasHtmlBody()) {
            msg.setContent(composeMultipartAlternative(mail));
        } else if (mail.hasHtmlBody() && mail.getHtmlBody().hasInline()){
            msg.setContent(composeMultipartRelated(mail));
        } else if (mail.hasTextBody()) {
            TextBody textBody = mail.getTextBody();
            msg.setText(textBody.getContent(), mimeCharset(textBody.getCharset()), TextBody.getSubType());
            if (textBody.getEncoding() != null) {
                msg.setHeader("Content-Transfer-Encoding", textBody.getEncoding());
            }
        } else if (mail.hasHtmlBody()) {
            HtmlBody htmlBody = mail.getHtmlBody();
            msg.setText(htmlBody.getContent(), mimeCharset(htmlBody.getCharset()), HtmlBody.getSubType());
            if (htmlBody.getEncoding() != null) {
                msg.setHeader("Content-Transfer-Encoding", htmlBody.getEncoding());
            }
        } else { // empty message
            msg.setText("");
        }
    }

    private String mimeCharset(Charset charset) {
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
        if (mail.hasTextBody() && mail.hasHtmlBody()) {
            addMultipart(mixed, composeMultipartAlternative(mail));
        } else if (mail.hasHtmlBody() && mail.getHtmlBody().hasInline()) {
            addMultipart(mixed, composeMultipartRelated(mail));
        } else if (mail.hasTextBody()) {
            mixed.addBodyPart(composeTextBody(mail));
        } else if (mail.hasHtmlBody()) {
            mixed.addBodyPart(composeHtmlBody(mail));
        }
        for (Attachment attachment : mail.getAttachments()) {
            mixed.addBodyPart(composeAttachment(attachment));
        }
        return mixed;
    }

    private Multipart composeMultipartAlternative(Mail mail) throws MessagingException, IOException {
        Multipart alternative = new MimeMultipart("alternative");
        alternative.addBodyPart(composeTextBody(mail));
        if (mail.getHtmlBody().hasInline()) {
            addMultipart(alternative, composeMultipartRelated(mail));
        } else {
            alternative.addBodyPart(composeHtmlBody(mail));
        }
        return alternative;
    }

    private Multipart composeMultipartRelated(Mail mail) throws MessagingException, IOException {
        Multipart related = new MimeMultipart("related");
        related.addBodyPart(composeHtmlBody(mail));
        for (Inline inline : mail.getHtmlBody().getInlines()) {
            related.addBodyPart(composeInline(inline));
        }
        return related;
    }

    private void addMultipart(Multipart dst, Multipart src) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(src);
        dst.addBodyPart(bodyPart);
    }

    private BodyPart composeTextBody(Mail mail) throws MessagingException {
        TextBody textBody = mail.getTextBody();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(textBody.getContent(), mimeCharset(textBody.getCharset()), TextBody.getSubType());
        if (textBody.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", textBody.getEncoding());
        }
        return bodyPart;
    }

    private BodyPart composeHtmlBody(Mail mail) throws MessagingException {
        HtmlBody htmlBody = mail.getHtmlBody();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(htmlBody.getContent(), mimeCharset(htmlBody.getCharset()), HtmlBody.getSubType());
        if (htmlBody.getEncoding() != null) {
            bodyPart.setHeader("Content-Transfer-Encoding", htmlBody.getEncoding());
        }
        return bodyPart;
    }

    private BodyPart composeAttachment(Attachment attachment) throws MessagingException, IOException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setDisposition("attachment");
        String mimeCharset = mimeCharset(attachment.getCharset());
        if (attachment.getFile() != null) {
            bodyPart.attachFile(attachment.getFile());
            setContentType(bodyPart, attachment.getMimeType(), mimeCharset);
        } else {
            String mimeType = (attachment.getMimeType() != null) ? attachment.getMimeType() : DEFAULT_MIME_TYPE;
            DataSource dataSource = new ByteArrayDataSource(attachment.getInputStream(), mimeType);
            bodyPart.setDataHandler(new DataHandler(dataSource));
            setContentType(bodyPart, mimeType, mimeCharset);
        }
        String encoding = (attachment.getEncoding() != null) ? attachment.getEncoding() : DEFAULT_ATTACHMENT_ENCODING;
        bodyPart.setHeader("Content-Transfer-Encoding", encoding);
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
            bodyPart.setHeader("Content-Id", "<" + inline.getId() + ">");
        }
        String encoding = (inline.getEncoding() != null) ? inline.getEncoding() : DEFAULT_INLINE_ENCODING;
        bodyPart.setHeader("Content-Transfer-Encoding", encoding);
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
