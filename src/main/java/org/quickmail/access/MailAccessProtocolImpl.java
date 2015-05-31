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
    private int connectionTimeout = 30000; // [ms]
    private int socketTimeout = 30000; // [ms]
    private String mailbox;
    private Store store;
    private Folder folder;

    public MailAccessProtocolImpl(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    @Override
    public void connect(String host, String user, String password) throws MessagingException {
        connect(host, getDefaultPort(), user, password);
    }

    private int getDefaultPort() {
        switch (protocol) {
            case PROTOCOL_POP3:
                return PORT_POP3;
            case PROTOCOL_IMAP:
                return PORT_IMAP;
            default:
                throw new AssertionError("Unknown protocol: " + protocol);
        }
    }

    @Override
    public void connect(String host, int port, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, port);
        Session session = Session.getInstance(props);
        try {
            store = session.getStore(protocol);
        } catch (NoSuchProviderException e) {
            throw new AssertionError("Unknown protocol: " + protocol);
        }
        store.connect(host, user, password);
        folder = store.getFolder(mailbox);
        folder.open(Folder.READ_WRITE);
    }

    private Properties createSessionProperties(String host, int port) {
        Properties props = new Properties();
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        props.put("mail." + protocol + ".connectiontimeout", connectionTimeout);
        props.put("mail." + protocol + ".timeout", socketTimeout);
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
    public int getNewMessageCount() throws MessagingException {
        if (folder == null || !folder.isOpen()) {
            throw new IllegalStateException("Not connecting to server");
        }
        return folder.getNewMessageCount();
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
}
