package com.auxolabs.scanner;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteChecker extends JFrame implements ActionListener, ChangeListener {


    private boolean displayAll = false;


    private JTextField url;
    private JTextField time;
    private JTextField mailId;
    private JTextField password;


    private JCheckBox toggleDisplayAll;
    private JButton check;
    private JPanel sitePanel;
    private JFrame frame;

    public SiteChecker() {
        super ( " Site Checker and Time Scheduling" );

        initComponents ();

        super.setLayout ( new FlowLayout () );
        super.setSize ( 600, 500 );
        super.setLocationRelativeTo ( null );
        super.setResizable ( false );
        super.setVisible ( true );

    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        SiteChecker psg = new SiteChecker ();
    }

    private final void initComponents() {


        this.url = new JTextField ( 40 );
        this.time = new JTextField ( 3 );

        this.mailId = new JTextField ( 20 );
        this.password = new JTextField ( 10 );

        this.toggleDisplayAll = new JCheckBox ( "Check Site With Time Schedule", true );
        this.toggleDisplayAll.addChangeListener ( this );


        this.check = new JButton ( "SUBMIT" );
        this.check.addActionListener ( this );

        this.sitePanel = new JPanel ( new FlowLayout () );
        this.sitePanel.setBorder ( BorderFactory.createTitledBorder ( "Site Checker" ) );

        this.sitePanel.setPreferredSize ( new Dimension ( 500, 400 ) );
        this.sitePanel.add ( new JLabel ( "Enter URL " ) );
        this.sitePanel.add ( this.url );

        this.sitePanel.add ( new JLabel ( "Enter time in minute: " ) );
        this.sitePanel.add ( this.time );

        this.sitePanel.add ( new JLabel ( " Enter Mail Id" ) );
        this.sitePanel.add ( this.mailId );

        this.sitePanel.add ( new JLabel ( " Enter Password" ) );
        this.sitePanel.add ( this.password );

        this.sitePanel.add ( this.toggleDisplayAll );
        this.sitePanel.add ( this.check );


        super.add ( this.sitePanel );

    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource () == this.check) {

            String url = this.url.getText ();
            String mailId = this.mailId.getText ();

            if (isValid ( url, mailId )) {
                try {
                    urlCheck ( this.url.getText (), this.time.getText (), this.mailId.getText (), this.password.getText () );
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }
            super.dispose ();
            new SiteChecker ();
        }
    }

    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource () == toggleDisplayAll) {
            this.displayAll = this.toggleDisplayAll.isSelected ();
        }

    }

    public void urlCheck(String url, String time, String mailId, String password) throws InterruptedException {

        int minute = Integer.parseInt ( time );
        try {
            URL obj = new URL ( url );
            URLConnection conn = obj.openConnection ();
            String server = conn.getHeaderField ( "Server" );
            for (int i = 1; i <= 3; i++) {
                if (server == null) {
                    timeSchedule ( minute );
                    System.out.println ( " 'Server' is down " );
                    sendMail ( mailId, password );
                } else {
                    System.out.println ( "Server - " + server );
                    JOptionPane.showMessageDialog ( frame, "Server is working !!!" );
                }
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void timeSchedule(int min) throws InterruptedException {
        TimeUnit.MINUTES.sleep ( min );
    }

    public void sendMail(final String mailId, final String mpassword) throws MessagingException {
        String recepient = mailId;
        Properties properties = new Properties ();
        properties.put ( "mail..smtp.auth", "true" );
        properties.put ( "mail.smtp.starttls.enable", "true" );
        properties.put ( "mail.smtp.host", "smtp.gmail.com" );
        properties.put ( "mail.smtp.port", "587" );
        properties.setProperty ( "mail.smtp.user", mailId );
        properties.setProperty ( "mail.smtp.password", mpassword );
        properties.setProperty ( "mail.smtp.auth", "true" );
        Session session = Session.getInstance ( properties, new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println ( "Email PassWord Authenticate Successfully" );
                return new PasswordAuthentication ( mailId, mpassword );
            }
        } );
        Message message = prepareMessage ( session, mailId, recepient );
        Transport.send ( message );
        JOptionPane.showMessageDialog ( frame, "Message Sent Successfully !!!!" );
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recepient) {

        String url = this.url.getText ();
        try {
            Message message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( myAccountEmail ) );
            message.setRecipient ( Message.RecipientType.TO, new InternetAddress ( recepient ) );
            message.setSubject ( "Site Checker" );
            message.setText ( url + " url is currently down" );
            return message;
        } catch (MessagingException e) {
            e.printStackTrace ();
        }
        return null;
    }

    public boolean isValid(String stringSite, String mailId) {
        Pattern patternS = Pattern.compile ( "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                "([).!';/?:,][[:blank:]])?$" );
        Matcher ms = patternS.matcher ( stringSite );
        boolean match2 = ms.matches ();
        Pattern patternM = Pattern.compile ( "^(.+)@(.+)$" );
        Matcher m = patternM.matcher ( mailId );
        boolean match = m.matches ();
        boolean match3 = Pattern.matches ( "^[1-5]?[0-9]", this.time.getText () );
        boolean result = false;
        if (!match2) {
            JOptionPane.showMessageDialog ( frame, " Enter Proper  Email Id Field !!!\n eg : auxo1234@gmail.com" );
        } else if (!match) {
            JOptionPane.showMessageDialog ( frame, " Enter Proper Url !!!" );
        } else if (!match3) {
            JOptionPane.showMessageDialog ( frame, " Enter Minute Proper  in 1-59 range " );
        } else result = true;
        return result;

    }


}