package org.quickmail.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import org.junit.Before;
import org.junit.Test;
import org.quickmail.Attachment;
import org.quickmail.HtmlBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextBody;
import org.quickmail.parser.DefaultMessageParser;
import org.quickmail.parser.MessageParser;

public class MessageComposerTest {
    private MessageComposer composer;
    private MessageParser parser;

    @Before
    public void setUp() throws Exception {
        composer = new MessageComposer(Session.getInstance(new Properties()));
        parser = new DefaultMessageParser();
    }

    @Test
    public void testCompose() throws MessagingException, IOException {
        // setup:
        String inlineContent = MailAssertUtils.EXPECTED_INLINE_CONTENT;
        String attachmentContent = MailAssertUtils.EXPECTED_ATTACHMENT_CONTENT;

        // when:
        Mail mail = new Mail()
                .setFrom("from@test.org")
                .addTo("to1@test.org", "to2@test.org")
                .addCc("cc1@test.org", "cc2@test.org")
                .addBcc("bcc1@test.org", "bcc2@test.org")
                .addReplyTo("replyto1@test.org", "replyto2@test.org")
                .setSentDate(new Date(1500000000000L))
                .setSubject("subject")
                .setTextBody(new TextBody("text body")
                        .setCharset(Charset.defaultCharset())
                        .setEncoding("7bit"))
                .setHtmlBody(new HtmlBody("html body")
                        .setCharset(Charset.defaultCharset())
                        .setEncoding("7bit")
                        .addInline(new Inline(new ByteArrayInputStream(inlineContent.getBytes()))
                                .setMimeType("image/png")
                                .setId("inline_id")
                                .setEncoding("base64")))
                .addAttachment(new Attachment(new ByteArrayInputStream(attachmentContent.getBytes()))
                        .setMimeType("text/plain")
                        .setCharset(Charset.defaultCharset())
                        .setName("attachment.txt")
                        .setEncoding("base64"));

        // and:
        Message msg = composer.compose(mail);

        // then:
        Mail parsedMail = parser.parse(msg);
        MailAssertUtils.assertMailStrictEquals(mail, parsedMail);
    }

    @Test
    public void testCompose_IfEmptyMail() throws MessagingException, IOException {
        // when:
        Mail mail = new Mail();

        // and:
        Message msg = composer.compose(mail);

        // then:
        Mail parsedMail = parser.parse(msg);
        MailAssertUtils.assertEmptyMailEquals(mail, parsedMail);
    }
}
