package org.quickmail.access;

import org.quickmail.Mail;

public interface MailAccessProtocol extends AutoCloseable {
    void connect(String host);
    void connect(String host, int port);
    void disconnect();
    boolean isConnected();
    void login(String userName, String password);
    int getMessageCount();
    Mail retrieveMessage(int id);
    void deleteMessage(int id);
    int getConnectionTimeout();
    void setConnectionTimeout(int timeout);
    int getSocketTimeout();
    void setSocketTimeout(int timeout);
}
