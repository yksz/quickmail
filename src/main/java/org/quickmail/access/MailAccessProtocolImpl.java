package org.quickmail.access;

import java.util.Objects;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.quickmail.Mail;
import org.quickmail.parser.MessageParser;
import org.quickmail.parser.ParserSelector;

class MailAccessProtocolImpl implements MailAccessProtocol {
    static final String PROTOCOL_POP3 = "pop3";
    static final String PROTOCOL_IMAP = "imap";
    static final int PORT_POP3 = 110;
    static final int PORT_POP3S = 995;
    static final int PORT_IMAP = 143;
    static final int PORT_IMAPS = 993;

    private final String protocol;
    private boolean sslEnabled;
    private boolean starttlsEnabled;
    private int connectionTimeout = 30000; // [ms]
    private int socketTimeout = 30000; // [ms]
    private String mailbox = "inbox";
    private Store store;
    private Folder folder;

    public MailAccessProtocolImpl(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void close() throws MessagingException {
        disconnect();
    }

    @Override
    public void connect(String host, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host);
        connect(props, user, password);
    }

    @Override
    public void connect(String host, int port, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, port, port);
        connect(props, user, password);
    }

    private void connect(Properties props, String user, String password) throws MessagingException {
        Session session = Session.getInstance(props);
        try {
            store = session.getStore(protocol);
        } catch (NoSuchProviderException e) {
            throw new AssertionError("Unknown protocol: " + protocol);
        }
        store.connect(user, password);
        folder = store.getFolder(mailbox);
        folder.open(Folder.READ_WRITE);
    }

    private Properties createSessionProperties(String host) {
        switch (protocol) {
        case PROTOCOL_POP3:
            return createSessionProperties(host, PORT_POP3, PORT_POP3S);
        case PROTOCOL_IMAP:
            return createSessionProperties(host, PORT_IMAP, PORT_IMAPS);
        default:
            throw new AssertionError("Unknown protocol: " + protocol);
        }
    }

    private Properties createSessionProperties(String host, int port, int sslPort) {
        Properties props = new Properties();
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        props.put("mail." + protocol + ".connectiontimeout", connectionTimeout);
        props.put("mail." + protocol + ".timeout", socketTimeout);
        props.put("mail." + protocol + ".starttls.enable", starttlsEnabled);
        if (sslEnabled) {
            props.put("mail." + protocol + ".socketFactory.port", sslPort);
            props.put("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail." + protocol + ".socketFactory.fallback", false);
        }
        return props;
    }

    @Override
    public void disconnect() throws MessagingException {
        if (folder != null && folder.isOpen()) {
            folder.close(true);
            folder = null;
        }
        if (store != null && store.isConnected()) {
            store.close();
            store = null;
        }
    }

    @Override
    public boolean isConnected() {
        if (store == null) {
            return false;
        }
        return store.isConnected();
    }

    @Override
    public int getMessageCount() throws MessagingException {
        if (folder == null || !folder.isOpen()) {
            throw new IllegalStateException("Not connecting to server");
        }
        return folder.getMessageCount();
    }

    @Override
    public Mail retrieveMessage(int messageNumber) throws MessagingException {
        if (folder == null || !folder.isOpen()) {
            throw new IllegalStateException("Not connecting to server");
        }
        return convertToMail(folder.getMessage(messageNumber));
    }

    private Mail convertToMail(Message message) throws MessagingException {
        ParserSelector selector = ParserSelector.getInstance();
        MessageParser parser = selector.selectParser(message);
        return parser.parse(message);
    }

    @Override
    public void deleteMessage(int messageNumber) throws MessagingException {
        if (folder == null || !folder.isOpen()) {
            throw new IllegalStateException("Not connecting to server");
        }
        Message message = folder.getMessage(messageNumber);
        message.setFlag(Flags.Flag.DELETED, true);
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public boolean isStarttlsEnabled() {
        return starttlsEnabled;
    }

    public void setStarttlsEnabled(boolean starttlsEnabled) {
        this.starttlsEnabled = starttlsEnabled;
    }

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    @Override
    public int getSocketTimeout() {
        return socketTimeout;
    }

    @Override
    public void setSocketTimeout(int timeout) {
        this.socketTimeout = timeout;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = Objects.requireNonNull(mailbox, "mailbox must not be null");
    }
}
