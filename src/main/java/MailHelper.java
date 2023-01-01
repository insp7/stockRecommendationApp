import java.net.InetAddress;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class MailHelper {
    final static String from = Constants.APPLICATION_MAILER;
    final  static String password = Constants.APPLICATION_MAILER_PASSWORD;

    static Session session = null;
    static InternetAddress addressFrom;
    static Transport transport;

    static {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //session.setDebug(true);
        try {
             transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
             addressFrom = new InternetAddress(from);
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendMail(String to,String subject,String msg) {
        try{
            MimeMessage message = new MimeMessage(session);
            message.setSender(addressFrom);
            message.setSubject(subject);
            message.setContent(msg, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            transport.connect();
            Transport.send(message);
            transport.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static boolean sendMailWithImagesToTeam(String subject,String msg,String screenShotBASE64) {
        try{
            MimeMessage message = new MimeMessage(session);
            message.setSender(addressFrom);
            message.setSubject(subject);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(Constants.RECIPIENT_MAIL_ID_1,Constants.RECIPIENT_MAIL_ID_2));

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = msg;
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);


            // second part (the image)
            messageBodyPart = new PreencodedMimeBodyPart("base64");
            messageBodyPart.setContent(screenShotBASE64,"image/*");
            messageBodyPart.setFileName("ScreenShot.png");

            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);


            transport.connect();
            Transport.send(message);
            transport.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendMailToAll(String to,String subject,String msg){
        try{
            MimeMessage message = new MimeMessage(session);
            message.setSender(addressFrom);
            message.setSubject(subject);
            message.setContent(msg, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            transport.connect();
            Transport.send(message);
            transport.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}