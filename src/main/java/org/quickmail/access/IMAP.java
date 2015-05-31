package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;

public class IMAP implements MailAccessProtocol {
    private MailAccessProtocol impl;

    public IMAP() {
        impl = new MailAccessProtocolImpl(MailAccessProtocolImpl.PROTOCOL_IMAP);
    }

    @Override
    public void close() throws Exception {
        impl.close();
    }

    @Override
    public void connect(String host, String user, String password) throws MessagingException {
        impl.connect(host, user, password);
    }

    @Override
    public void connect(String host, int port, String user, String password) throws MessagingException {
        impl.connect(host, port, user, password);
    }

    @Override
    public void disconnect() throws MessagingException {
        impl.disconnect();
    }

    @Override
    public boolean isConnected() {
        return impl.isConnected();
    }

    @Override
    public int getMessageCount() throws MessagingException {
        return impl.getMessageCount();
    }

    @Override
    public int getNewMessageCount() throws MessagingException {
        return impl.getNewMessageCount();
    }

    @Override
    public Mail retrieveMessage(int messageNumber) throws MessagingException {
        return impl.retrieveMessage(messageNumber);
    }

    @Override
    public void deleteMessage(int messageNumber) throws MessagingException {
        impl.deleteMessage(messageNumber);
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
