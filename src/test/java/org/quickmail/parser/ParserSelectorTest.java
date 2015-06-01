package org.quickmail.parser;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;

public class ParserSelectorTest {
    private ParserSelector selector = ParserSelector.getInstance();

    @Before
    public void setUp() throws Exception {
        selector.clearRules();
    }

    @Test
    public void testSelectParser_WhenRulesAreDefinedAndMatched() throws MessagingException {
        // setup:
        Message message = new MimeMessage(Session.getInstance(new Properties()));

        // when:
        message.addHeader("X-Mailer", "Microsoft Office Outlook 11");

        // and:
        selector.addRule(new ParserSelectingRule("X-Mailer", "Outlook", new OutlookParser()));

        // then:
        MessageParser parser = selector.selectParser(message);
        assertTrue(parser instanceof OutlookParser);
    }

    @Test
    public void testSelectParser_WhenRulesAreDefinedAndNotMatched() throws MessagingException {
        // setup:
        Message message = new MimeMessage(Session.getInstance(new Properties()));

        // when:
        message.addHeader("From", "foobar@gmail.com");

        // and:
        selector.addRule(new ParserSelectingRule("X-Mailer", "Outlook", new OutlookParser()));

        // then:
        MessageParser parser = selector.selectParser(message);
        assertTrue(parser instanceof DefaultMessageParser);
    }

    @Test
    public void testSelectParser_WhenRulesAreNotDefined() throws MessagingException {
        // setup:
        Message message = new MimeMessage(Session.getInstance(new Properties()));

        // when:
        message.addHeader("X-Mailer", "Microsoft Office Outlook 11");

        // then:
        MessageParser parser = selector.selectParser(message);
        assertTrue(parser instanceof DefaultMessageParser);
    }
}
