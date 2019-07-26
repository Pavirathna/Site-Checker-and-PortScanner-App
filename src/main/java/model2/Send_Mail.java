package model2;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static javax.mail.Transport.send;

public class Send_Mail {
    public static void main(String[] args) {

        String to = "pvthra027@gmail.com";
        String from = "pavirathna077@gmail.com";
        String host = "localhost";
        Properties properties = System.getProperties ();
        properties.setProperty ( "mail.smtp.host", host );
        Session session = Session.getDefaultInstance ( properties );
        try {
            MimeMessage message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( from ) );
            message.addRecipient ( Message.RecipientType.TO, new InternetAddress ( to ) );
            message.setSubject ( "This is the Subject Line!" );
            message.setText ( "This is actual message" );
            send ( message );
            System.out.println ( "Sent message successfully...." );
        } catch (MessagingException mex) {
            mex.printStackTrace ();
        }
    }
}
