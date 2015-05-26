package org.quickmail.access;

import org.quickmail.Mail;

public class IMAP implements MailAccessProtocol {
    private MailAccessProtocol impl;

    public IMAP() {
        impl = new MailAccessProtocolImpl(MailAccessProtocolImpl.IMAP);
    }

    @Override
    public void close() throws Exception {
        impl.close();
    }

    @Override
    public void connect(String host) {
        impl.connect(host);
    }

    @Override
    public void connect(String host, int port) {
        impl.connect(host, port);
    }

    @Override
    public void disconnect() {
        impl.disconnect();
    }

    @Override
    public boolean isConnected() {
        return impl.isConnected();
    }

    @Override
    public void login(String userName, String password) {
        impl.login(userName, password);
    }

    @Override
    public int getMessageCount() {
        return impl.getMessageCount();
    }

    @Override
    public Mail retrieveMessage(int id) {
        return impl.retrieveMessage(id);
    }

    @Override
    public void deleteMessage(int id) {
        impl.deleteMessage(id);
    }

    @Override
    public int getConnectionTimeout() {
        return impl.getConnectionTimeout();
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        impl.setConnectionTimeout(timeout);
    }

    @Override
    public int getSocketTimeout() {
        return impl.getSocketTimeout();
    }

    @Override
    public void setSocketTimeout(int timeout) {
        impl.setSocketTimeout(timeout);
    }
}
