package org.quickmail.access;

import org.quickmail.Mail;

class MailAccessProtocolImpl implements MailAccessProtocol {
    static final String POP3 = "pop3";
    static final String IMAP = "imap";

    private String protocol;

    public MailAccessProtocolImpl(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    @Override
    public void connect(String host) {
    }

    @Override
    public void connect(String host, int port) {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void login(String userName, String password) {
    }

    @Override
    public int getMessageCount() {
        return 0;
    }

    @Override
    public Mail retrieveMessage(int id) {
        return null;
    }

    @Override
    public void deleteMessage(int id) {
    }

    @Override
    public int getConnectionTimeout() {
        return 0;
    }

    @Override
    public void setConnectionTimeout(int timeout) {
    }

    @Override
    public int getSocketTimeout() {
        return 0;
    }

    @Override
    public void setSocketTimeout(int timeout) {
    }
}
