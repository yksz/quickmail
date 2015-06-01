package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;

public interface MailAccessProtocol extends AutoCloseable {
    void close() throws MessagingException;
    void connect(String host, String user, String password) throws MessagingException;
    void connect(String host, int port, String user, String password) throws MessagingException;
    void disconnect() throws MessagingException;
    boolean isConnected();
    int getMessageCount() throws MessagingException;
    Mail retrieveMessage(int messageNumber) throws MessagingException;
    void deleteMessage(int messageNumber) throws MessagingException;
    boolean isSslEnabled();
    void setSslEnabled(boolean sslEnabled);
    boolean isStarttlsEnabled();
    void setStarttlsEnabled(boolean starttlsEnabled);
    int getConnectionTimeout();
    void setConnectionTimeout(int timeout);
    int getSocketTimeout();
    void setSocketTimeout(int timeout);
}
