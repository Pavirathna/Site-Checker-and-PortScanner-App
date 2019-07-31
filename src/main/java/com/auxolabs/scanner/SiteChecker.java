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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteChecker extends JFrame implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 2884600754343147821L;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 375;


    private boolean displayAll = false;


    private JTextField site;
    private JTextField time;
    private JTextField mailId;
    private JTextField password;


    private JCheckBox toggleDisplayAll;
    private JButton check;
    private JPanel settingsPanel;
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


        this.site = new JTextField ( 40 );
        this.time = new JTextField ( 3 );

        this.mailId = new JTextField ( 20 );
        this.password = new JTextField ( 10 );

        this.toggleDisplayAll = new JCheckBox ( "Check Site With Time Schedule", true );
        this.toggleDisplayAll.addChangeListener ( this );


        this.check = new JButton ( "SUBMIT" );
        this.check.addActionListener ( this );

        this.settingsPanel = new JPanel ( new FlowLayout () );
        this.settingsPanel.setBorder ( BorderFactory.createTitledBorder ( "Site Checker" ) );

        this.settingsPanel.setPreferredSize ( new Dimension ( 500, 400 ) );
        this.settingsPanel.add ( new JLabel ( "Enter site " ) );
        this.settingsPanel.add ( this.site );

        this.settingsPanel.add ( new JLabel ( "Enter time in minute: " ) );
        this.settingsPanel.add ( this.time );

        this.settingsPanel.add ( new JLabel ( " Enter Mail Id" ) );
        this.settingsPanel.add ( this.mailId );

        this.settingsPanel.add ( new JLabel ( " Enter Password" ) );
        this.settingsPanel.add ( this.password );

        this.settingsPanel.add ( this.toggleDisplayAll );
        this.settingsPanel.add ( this.check );


        super.add ( this.settingsPanel );

    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource () == this.check) {

            String site = this.site.getText ();
            String mailId = this.mailId.getText ();

            if (isValid ( site, mailId )) {
                System.out.println ( isValid ( site, mailId ) );
                try {
                    siteCheck ( this.site.getText (), this.time.getText (), this.mailId.getText (), this.password.getText () );
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            } else JOptionPane.showMessageDialog ( frame, " Enter Proper Field !!!" );
        }
    }

    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource () == toggleDisplayAll) {
            this.displayAll = this.toggleDisplayAll.isSelected ();
        }

    }

    public void siteCheck(String site, String time, String mailId, String password) throws InterruptedException {

        int minute = Integer.parseInt ( time );
        try {
            URL obj = new URL ( site );
            URLConnection conn = obj.openConnection ();
            String server = conn.getHeaderField ( "Server" );
            for (int i = 1; i <= 3; i++) {
                if (server == null) {
                    timeSchedule ( minute );
                    System.out.println ( " 'Server' is down " );
                    sendMail ( mailId, password );
                } else {
                    System.out.println ( "Server - " + server );
                }
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void timeSchedule(int min) throws InterruptedException {

        TimeUnit.MINUTES.sleep ( min );
        DateFormat df = new SimpleDateFormat ( "dd/MM/yy HH:mm:ss" );
        Date dateob = new Date ();
        System.out.println ( df.format ( dateob ) );

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

        String site = this.site.getText ();
        try {
            Message message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( myAccountEmail ) );
            message.setRecipient ( Message.RecipientType.TO, new InternetAddress ( recepient ) );
            message.setSubject ( "Site Checker" );
            message.setText ( site + " site is currently down" );
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
        if (!match) {
            JOptionPane.showMessageDialog ( frame, " Enter Proper  Email Id Field !!!\n eg : auxo1234@gmail.com" );
        } else if (!match2) {
            JOptionPane.showMessageDialog ( frame, " Enter Proper Url !!!" );
        } else if (!match3) {
            JOptionPane.showMessageDialog ( frame, " Enter Minute Proper  in 1-59 range " );
        } else result = true;


        return result;

    }

}