package org.quickmail.transport;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

import org.quickmail.Mail;
import org.quickmail.composer.MessageComposer;

public class SMTP implements AutoCloseable {
    static final String PROTOCOL_SMTP = "smtp";
    static final String PROTOCOL_POP3 = "pop3";
    static final int PORT_SMTP = 25;
    static final int PORT_SMTPS = 465;
    static final int PORT_POP3 = 110;
    static final int PORT_POP3S = 995;

    private final String protocol;
    private boolean sslEnabled;
    private boolean starttlsEnabled;
    private int connectionTimeout = 30000; // [ms]
    private int socketTimeout = 30000; // [ms]
    private Session session;
    private Transport transport;
    private Store store;

    public SMTP() {
        this.protocol = PROTOCOL_SMTP;
    }

    @Override
    public void close() throws MessagingException {
        disconnect();
    }

    public void connect(String host) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, PORT_SMTP, PORT_SMTPS, false);
        session = Session.getInstance(props);
        connect(session);
    }

    public void connect(String host, int port) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, port, port, false);
        session = Session.getInstance(props);
        connect(session);
    }

    public void connect(String host, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, PORT_SMTP, PORT_SMTPS, true);
        session = Session.getInstance(props);
        connect(session, user, password);
    }

    public void connect(String host, int port, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Properties props = createSessionProperties(host, port, port, true);
        session = Session.getInstance(props);
        connect(session, user, password);
    }

    private void connect(Session session) throws MessagingException {
        transport = session.getTransport(protocol);
        transport.connect();
    }

    private void connect(Session session, String user, String password) throws MessagingException {
        transport = session.getTransport(protocol);
        transport.connect(user, password);
    }

    private Properties createSessionProperties(String host, int port, int sslPort, boolean auth) {
        Properties props = new Properties();
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        props.put("mail." + protocol + ".connectiontimeout", connectionTimeout);
        props.put("mail." + protocol + ".timeout", socketTimeout);
        props.put("mail." + protocol + ".auth", auth); // SMTP-AUTH
        props.put("mail." + protocol + ".starttls.enable", starttlsEnabled);
        if (sslEnabled) {
            props.put("mail." + protocol + ".socketFactory.port", sslPort);
            props.put("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail." + protocol + ".socketFactory.fallback", false);
        }
        return props;
    }

    /**
     * Connect By POP before SMTP.
     *
     * @param host
     * @param pop3Host
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void connectByPbS(String host, String pop3Host, String user, String password)
            throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Objects.requireNonNull(pop3Host, "pop3Host must not be null");
        Properties props = createSessionProperties(host, PORT_SMTP, PORT_SMTPS, false);
        session = Session.getInstance(props);
        int pop3Port = sslEnabled ? PORT_POP3S : PORT_POP3;
        connectByPOP3(session, pop3Host, pop3Port, user, password);
        connect(session);
    }

    /**
     * Connect By POP before SMTP.
     *
     * @param host
     * @param port
     * @param pop3Host
     * @param pop3Port
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void connectByPbS(String host, int port, String pop3Host, int pop3Port, String user, String password)
            throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        Objects.requireNonNull(pop3Host, "pop3Host must not be null");
        Properties props = createSessionProperties(host, port, port, false);
        session = Session.getInstance(props);
        connectByPOP3(session, pop3Host, pop3Port, user, password);
        connect(session);
    }

    private void connectByPOP3(Session session, String host, int port, String user, String password)
            throws MessagingException {
        store = session.getStore(PROTOCOL_POP3);
        store.connect(host, port, user, password);
    }

    public void disconnect() throws MessagingException {
        if (transport != null && transport.isConnected()) {
            transport.close();
            transport = null;
        }
        if (store != null && store.isConnected()) {
            store.close();
            store = null;
        }
    }

    public boolean isConnected() {
        if (transport == null) {
            return false;
        }
        return transport.isConnected();
    }

    public void send(Message message) throws MessagingException {
        Objects.requireNonNull(message, "message must not be null");
        if (session == null || transport == null || !transport.isConnected()) {
            throw new IllegalStateException("Not connecting to server");
        }
        transport.sendMessage(message, message.getAllRecipients());
    }

    public void send(Mail mail) throws MessagingException, IOException {
        Objects.requireNonNull(mail, "mail must not be null");
        if (session == null || transport == null || !transport.isConnected()) {
            throw new IllegalStateException("Not connecting to server");
        }
        Message message = new MessageComposer(session).compose(mail);
        transport.sendMessage(message, message.getAllRecipients());
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

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int timeout) {
        this.socketTimeout = timeout;
    }
}
