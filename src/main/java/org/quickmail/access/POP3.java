package org.quickmail.access;

import javax.mail.MessagingException;

import org.quickmail.Mail;

public class POP3 implements MailAccessProtocol {
    private MailAccessProtocolImpl impl;

    public POP3() {
        impl = new MailAccessProtocolImpl(MailAccessProtocolImpl.PROTOCOL_POP3);
    }

    @Override
    public void close() throws MessagingException {
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
    public Mail retrieveMessage(int messageNumber) throws MessagingException {
        return impl.retrieveMessage(messageNumber);
    }

    @Override
    public void deleteMessage(int messageNumber) throws MessagingException {
        impl.deleteMessage(messageNumber);
    }

    @Override
    public boolean isSslEnabled() {
        return impl.isSslEnabled();
    }

    @Override
    public void setSslEnabled(boolean sslEnabled) {
        impl.setSslEnabled(sslEnabled);
    }

    @Override
    public boolean isStarttlsEnabled() {
        return impl.isStarttlsEnabled();
    }

    @Override
    public void setStarttlsEnabled(boolean starttlsEnabled) {
        impl.setStarttlsEnabled(starttlsEnabled);
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
