package com.example.vavagoinventory;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class SendEmail {
    public void send(String ToEmail, String Subject, String Text) {

        String Msg;
        final String username = "xxxxx@gmail.com";
        final String password = "xxxxxxxxx";
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("xxxxxxx.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ToEmail));
            message.setSubject(Subject);
            message.setText(Text);
            Transport.send(message);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();

        }
    }
}


