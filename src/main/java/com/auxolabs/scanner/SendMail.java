package com.auxolabs.scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {
    public static void sendMail() throws MessagingException {
        String recepient = "auxodev1234@gmail.com";
        Properties properties = new Properties ();
        properties.put ( "mail..smtp.auth", "true" );
        properties.put ( "mail.smtp.starttls.enable", "true" );
        properties.put ( "mail.smtp.host", "smtp.gmail.com" );
        properties.put ( "mail.smtp.port", "587" );
        final String myAccountEmail = "auxodev1234@gmail.com";
        final String passWord = "auxo1234";
        properties.setProperty ( "mail.smtp.user", "auxodev1234" );
        properties.setProperty ( "mail.smtp.password", "auxo1234" );
        properties.setProperty ( "mail.smtp.auth", "true" );
        Session session = Session.getInstance ( properties, new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println ( "Email PassWord Authenticate Successfully" );
                return new PasswordAuthentication ( myAccountEmail, passWord );
            }
        } );
        Message message = prepareMessage ( session, myAccountEmail, recepient );
        Transport.send ( message );
        System.out.println ( "Message sent successfully!!" );

    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {

        try {
            Message message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( myAccountEmail ) );
            message.setRecipient ( Message.RecipientType.TO, new InternetAddress ( recepient ) );
            message.setSubject ( "Site Checker" );
            message.setText ( "That Site was down" );
            return message;
        } catch (MessagingException e) {
            e.printStackTrace ();
        }
        return null;
    }
}