package org.quickmail.transport;

import java.util.Objects;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

import org.quickmail.Mail;

public class SMTP implements AutoCloseable {
    private static final String PROTOCOL_SMTP = "smtp";
    private static final int PORT_SMTP = 25;
    private static final int PORT_SMTPS = 465;

    private final String protocol;
    private final MessageComposer composer;
    private int connectionTimeout = 30000; // [ms]
    private int socketTimeout = 30000; // [ms]
    private Transport transport;

    public SMTP() {
        this.protocol = PROTOCOL_SMTP;
        this.composer = new MessageComposer();
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    public void connect(String host) throws MessagingException {
        connect(host, getDefaultPort());
    }

    public void connect(String host, int port) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        transport = getTransport(host, port, false);
        transport.connect();
    }

    public void connect(String host, String user, String password) throws MessagingException {
        connect(host, getDefaultPort(), user, password);
    }

    public void connect(String host, int port, String user, String password) throws MessagingException {
        Objects.requireNonNull(host, "host must not be null");
        transport = getTransport(host, port, true);
        transport.connect(user, password);
    }

    private int getDefaultPort() {
        return PORT_SMTP;
    }

    private Transport getTransport(String host, int port, boolean auth) {
        Properties props = createSessionProperties(host, port, auth);
        Session session = Session.getInstance(props);
        try {
            return session.getTransport(protocol);
        } catch (NoSuchProviderException e) {
            throw new AssertionError("Unknown protocol: " + protocol);
        }
    }

    private Properties createSessionProperties(String host, int port, boolean auth) {
        Properties props = new Properties();
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        props.put("mail." + protocol + ".connectiontimeout", connectionTimeout);
        props.put("mail." + protocol + ".timeout", socketTimeout);
        props.put("mail." + protocol + ".auth", auth);
        return props;
    }

    public void disconnect() throws MessagingException {
        if (transport != null && transport.isConnected()) {
            transport.close();
            transport = null;
        }
    }

    public boolean isConnected() {
        if (transport == null) {
            return false;
        }
        return transport.isConnected();
    }

    public void send(Mail mail) throws MessagingException {
        Objects.requireNonNull(mail, "mail must not be null");
        if (transport == null || !transport.isConnected()) {
            throw new IllegalStateException("Not connecting to server");
        }
        Message message = composer.compose(mail);
        Address[] addrs = message.getAllRecipients();
        transport.sendMessage(message, addrs);
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
