package org.quickmail;

public class QuickMailException extends Exception {
    public QuickMailException() {
        super();
    }

    public QuickMailException(String message) {
        super(message);
    }

    public QuickMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuickMailException(Throwable cause) {
        super(cause);
    }
}
