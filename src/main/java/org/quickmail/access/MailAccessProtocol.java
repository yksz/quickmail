package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;

public interface MailAccessProtocol extends AutoCloseable {
    void connect(String host, String user, String password) throws MessagingException;
    void connect(String host, int port, String user, String password) throws MessagingException;
    void disconnect() throws MessagingException;
    boolean isConnected();
    int getMessageCount() throws MessagingException;
    int getNewMessageCount() throws MessagingException;
    Mail retrieveMessage(int messageNumber) throws MessagingException;
    void deleteMessage(int messageNumber) throws MessagingException;
    int getConnectionTimeout();
    void setConnectionTimeout(int timeout);
    int getSocketTimeout();
    void setSocketTimeout(int timeout);
}
