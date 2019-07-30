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

public class SiteChecker extends JFrame implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 2884600754343147821L;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 375;


    private boolean displayAll = false;


    private JTextField site;
    private JTextField time;
    private JTextField mailId;
    private JTextField password;

    private JTextArea output;
    private JCheckBox toggleDisplayAll;
    private JButton check;
    private JPanel settingsPanel;
    private JFrame frame;

    public SiteChecker() {
        super ( " Site Checker and Time Scheduling" );

        initComponents ();

        super.setLayout ( new FlowLayout () );
        super.setSize ( 300, 500 );
        super.setLocationRelativeTo ( null );
        super.setResizable ( false );
        super.setVisible ( true );
        super.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
//        frame = new JFrame("Show Message Box");
//        JButton button = new JButton("Click");
//        // button.setAlignmentX((float) 100.00);
//
//        button.addActionListener(null);
//        frame.add(button);
//        frame.setSize(400, 200);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private final void initComponents() {


        this.site = new JTextField ( 20 );
        this.time = new JTextField ( 3 );

        this.mailId=new  JTextField ( 20 );
        this.password = new JTextField ( 10 );

        this.output = new JTextArea ( 10, 20 );
        this.output.setEditable ( false );
        this.output.setLineWrap ( true );


        this.toggleDisplayAll = new JCheckBox ( "Check Site With Time Schedule" );
        this.toggleDisplayAll.addChangeListener ( this );


        this.check = new JButton ( "SUBMIT" );
        this.check.addActionListener ( this );

        this.settingsPanel = new JPanel ( new FlowLayout () );
        this.settingsPanel.setBorder ( BorderFactory.createTitledBorder ( "Site Checker" ) );

        this.settingsPanel.setPreferredSize ( new Dimension ( 230, 230 ) );
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


            try {
                siteCheck(this.site.getText (),this.time.getText (),this.mailId.getText (),this.password.getText ());
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

        }
    }

    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource () == toggleDisplayAll) {
            this.displayAll = this.toggleDisplayAll.isSelected ();
       }

    }

    public  void siteCheck(String site,String time,String mailId,String password) throws InterruptedException {

        int minute = Integer.parseInt(time);
        timeSchedule ( minute );
        try {
            URL obj = new URL (site);//( "http://tamilrockers.co.com/" );
            URLConnection conn = obj.openConnection ();
            String server = conn.getHeaderField ( "Server" );
            if (server == null) {
                System.out.println ( " 'Server' is down " );
                sendMail (mailId,password);
                JOptionPane.showMessageDialog(frame," Message Send Successfully !!!");
            } else {
                System.out.println ( "Server - " + server );
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void timeSchedule(int min) throws InterruptedException {

        for(int i=0;i<3;i++) {
            TimeUnit.MINUTES.sleep ( min );
            DateFormat df = new SimpleDateFormat ( "dd/MM/yy HH:mm:ss" );
            Date dateob = new Date ();
            System.out.println ( df.format ( dateob ) );
            SiteCheckerPro sc=new SiteCheckerPro ();
            sc.siteCheck ();

        }
    }
    public  void sendMail(final String mailId, final String mpassword) throws MessagingException {
        String recepient =mailId;
        Properties properties = new Properties ();
        properties.put ( "mail..smtp.auth", "true" );
        properties.put ( "mail.smtp.starttls.enable", "true" );
        properties.put ( "mail.smtp.host", "smtp.gmail.com" );
        properties.put ( "mail.smtp.port", "587" );
       // final String myAccountEmail = mailId;
    //    final String passWord = mpassword;
        properties.setProperty ( "mail.smtp.user", mailId );
        properties.setProperty ( "mail.smtp.password", mpassword );
        properties.setProperty ( "mail.smtp.auth", "true" );
        Session session = Session.getInstance ( properties, new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println ( "Email PassWord Authenticate Successfully" );
                return new PasswordAuthentication (mailId, mpassword );
            }
        } );
        Message message = prepareMessage ( session, mailId, recepient );
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
    public static void main (String[]args){
        @SuppressWarnings("unused")
        SiteChecker psg = new SiteChecker ();
    }

}