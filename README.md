QuickMail
==========
QuickMail is a simple JavaMail wrapper.  
This library can parse email messages more quickly.


Example - Send Mail
===================
```java
    try (SMTP smtp = new SMTP()) {
        smtp.connect("smtp.example.org");
        smtp.send(new Mail()
                .addTo("foobar@example.org")
                .setFrom("no-reply@example.org")
                .setSubject("SMTP Sample"));
    }
```

Example - Reply Mail
====================
```java
    try (IMAP imap = new IMAP(); SMTP smtp = new SMTP()) {
        imap.setSslEnabled(true);
        imap.connect("imap.example.org", "user", "password");
        int msgCount = imap.getMessageCount();
        if (msgCount > 0) {
            Mail mail = imap.retrieveMail(msgCount);
            mail.clearTo()
                    .addTo(mail.getReplyTo())
                    .setFrom("no-reply@example.org")
                    .setSubject("Re:" + mail.getSubject());
            smtp.setSslEnabled(true);
            smtp.connect("smtp.example.org", "user", "password");
            smtp.send(mail);
        }
    }
```

System Requirements
===================
- JDK version 1.7 or above


License
=======
QuickMail is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).

