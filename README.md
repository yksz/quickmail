QuickMail
==========
QuickMail is a simple JavaMail wrapper.  
This library can parse email messages more quickly.

Installation
============
### Gradle
```
repositories {
    maven { url 'http://yksz.github.io/maven/repository' }
}

dependencies {
    compile 'org.quickmail:quickmail:0.1.0'
}
```

### Maven
```xml
<repository>
    <url>http://yksz.github.io/maven/repository</url>
</repository>

<dependency>
    <groupId>org.quickmail</groupId>
    <artifactId>quickmail</artifactId>
    <version>0.1.0</version>
</dependency>
```

Example - Receive Mail
===================
```java
try (POP3 pop3 = new POP3()){
    pop3.connect("pop3.example.org", "user", "password");
    int msgCount = pop3.getMessageCount();
    if (msgCount > 0) {
        Mail mail = pop3.retrieveMail(msgCount);
        System.out.println("subject: " + mail.getSubject());
        for (Attachment attachment : mail.getAttachments()) {
            System.out.println(" attachment: " + attachment.getName());
        }
    }
}
```

Example - Send Mail
===================
```java
try (SMTP smtp = new SMTP()) {
    smtp.connect("smtp.example.org");
    smtp.send(new Mail()
            .addTo("foobar@example.org")
            .setFrom("no-reply@example.org")
            .setSubject("SMTP Sample")
            .addAttachment(new Attachment(new File("file.txt"))));
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
JDK version 1.7 or above

License
=======
QuickMail is released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html).
