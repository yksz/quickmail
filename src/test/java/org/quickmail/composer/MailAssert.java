package org.quickmail.composer;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.mail.Header;

import org.quickmail.Attachment;
import org.quickmail.HtmlBody;
import org.quickmail.Inline;
import org.quickmail.Mail;
import org.quickmail.TextBody;

class MailAssert {
    static final String EXPECTED_INLINE_CONTENT = "inline";
    static final String EXPECTED_ATTACHMENT_CONTENT = "attachment";

    static <T> void assertListEquals(List<T> expected, List<T> actual) {
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    static void assertMailStrictEquals(Mail expected, Mail actual) throws IOException {
        assertHeadersEquals(expected.getHeaders(), actual.getHeaders());
        assertEquals(expected.getFrom(), actual.getFrom());
        assertListEquals(expected.getTo(), actual.getTo());
        assertListEquals(expected.getCc(), actual.getCc());
        assertListEquals(expected.getBcc(), actual.getBcc());
        assertListEquals(expected.getReplyTo(), actual.getReplyTo());
        assertEquals(expected.getSentDate(), actual.getSentDate());
        assertEquals(expected.getSubject(), actual.getSubject());
        assertEquals(expected.getSubjectCharset(), actual.getSubjectCharset());
        assertTextBodyEquals(expected.getTextBody(), actual.getTextBody());
        assertHtmlBodyEquals(expected.getHtmlBody(), actual.getHtmlBody());
        assertAttachmentsEquals(expected.getAttachments(), actual.getAttachments());
    }

    static void assertHeadersEquals(List<Header> expected, List<Header> actual) {
        for (Header header : expected) {
            String actualValue = getHeader(actual, header.getName());
            assertEquals(header.getValue(), actualValue);
        }
    }

    private static String getHeader(List<Header> headers, String name) {
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                return header.getValue();
            }
        }
        return null;
    }

    static void assertTextBodyEquals(TextBody expected, TextBody actual) {
        if (expected != null && actual != null) {
            assertEquals(expected.getContent(), actual.getContent());
            assertEquals(expected.getCharset(), actual.getCharset());
            assertEquals(expected.getEncoding(), actual.getEncoding());
        } else if (expected == null && actual == null) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    static void assertHtmlBodyEquals(HtmlBody expected, HtmlBody actual) throws IOException {
        if (expected != null && actual != null) {
            assertEquals(expected.getContent(), actual.getContent());
            assertEquals(expected.getCharset(), actual.getCharset());
            assertEquals(expected.getEncoding(), actual.getEncoding());
            assertInlinesEquals(expected.getInlines(), actual.getInlines());
        } else if (expected == null && actual == null) {
            assertTrue(true);
        } else {
            fail();
        }
    }

    static void assertInlinesEquals(List<Inline> expected, List<Inline> actual) throws IOException {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertInlineEquals(expected.get(i), actual.get(i));
        }
    }

    static void assertInlineEquals(Inline expected, Inline actual) throws IOException {
        assertEquals(EXPECTED_INLINE_CONTENT, toString(actual.getInputStream()));
        assertEquals(expected.getMimeType(), actual.getMimeType());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEncoding(), actual.getEncoding());
    }

    static void assertAttachmentsEquals(List<Attachment> expected, List<Attachment> actual) throws IOException {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertAttachmentEquals(expected.get(i), actual.get(i));
        }
    }

    static void assertAttachmentEquals(Attachment expected, Attachment actual) throws IOException {
        assertEquals(EXPECTED_ATTACHMENT_CONTENT, toString(actual.getInputStream()));
        assertEquals(expected.getMimeType(), actual.getMimeType());
        assertEquals(expected.getCharset(), actual.getCharset());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getNameCharset(), actual.getNameCharset());
        assertEquals(expected.getEncoding(), actual.getEncoding());
    }

    private static String toString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream bin = new BufferedInputStream(in);
        Reader r = new InputStreamReader(bin);
        char[] buf = new char[1024];
        int len;
        while ((len = r.read(buf)) != -1) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    static void assertEmptyMailEquals(Mail expected, Mail actual) throws IOException {
        assertEquals(expected.getFrom(), actual.getFrom());
        assertListEquals(expected.getTo(), actual.getTo());
        assertListEquals(expected.getCc(), actual.getCc());
        assertListEquals(expected.getBcc(), actual.getBcc());
        assertListEquals(expected.getReplyTo(), actual.getReplyTo());
        assertNotNull(actual.getSentDate());
        assertEquals(expected.getSubject(), actual.getSubject());
        assertEquals(expected.getSubjectCharset(), actual.getSubjectCharset());
        assertEmptyMailTextBody(actual.getTextBody());
        assertHtmlBodyEquals(expected.getHtmlBody(), actual.getHtmlBody());
        assertAttachmentsEquals(expected.getAttachments(), actual.getAttachments());
    }

    static void assertEmptyMailTextBody(TextBody actual) {
        assertNotNull(actual);
        assertEquals("", actual.getContent());
        assertNotNull(actual.getCharset());
        assertEquals("7bit", actual.getEncoding());
    }
}
