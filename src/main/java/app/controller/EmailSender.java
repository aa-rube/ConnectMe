package app.controller;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
    public static String sendAuthCode(String toEmail, String code) {
        String fromEmail = "9533580733@mail.ru";
        String password = "wuPWkgprETjKeVhP6jmX";
        String subject = "ConnectMe | Ваш пароль для входа в аккаунт";
        String codeStr = " Ваш код подтверждения: " + code;
        String body = codeStr + "\n\nСообщение сгененрировано автоматически, пожалуйста не отвечайте на него";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            message.setContent(multipart);

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(body);
            multipart.addBodyPart(textBodyPart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;
    }
}
