package com.auxo.scanner;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteChecker extends JFrame implements ActionListener {

    static List<String> recipients = new ArrayList<> ();
    static List<String> urls = new ArrayList<> ();
    private JTextField url;
    private JTextField time;
    private JTextField recipient;
    private JButton check, cancel, addRecipient, addUrl;
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

    private final void initComponents() {


        this.url = new JTextField ( 44 );
        this.time = new JTextField ( 3 );
        this.addUrl = new JButton ( "+" );
        this.recipient = new JTextField ( 27 );
        this.addRecipient = new JButton ( "+" );
        this.cancel = new JButton ( "CANCEL" );
        this.check = new JButton ( "SUBMIT" );
        this.check.addActionListener ( this );

        this.sitePanel = new JPanel ( new FlowLayout () );
        this.sitePanel.setBorder ( BorderFactory.createTitledBorder ( "Site Checker" ) );

        this.sitePanel.setPreferredSize ( new Dimension ( 600, 400 ) );
        this.sitePanel.add ( new JLabel ( "Enter URL " ) );
        this.sitePanel.add ( this.url );
        this.sitePanel.add ( this.addUrl );
        addUrl.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ce) {

                if (isValid ( url.getText () )) {
                    urls.add ( url.getText () );
                    JOptionPane.showMessageDialog ( frame, url.getText () + "  url  added successfully" );
                    url.setText ( "enter url " );
                } else {
                    JOptionPane.showMessageDialog ( frame, "Enter Proper Url" );
                    url.setText ( "" );
                }


            }
        } );

        this.sitePanel.add ( new JLabel ( "Enter time in minute " ) );
        this.sitePanel.add ( this.time );

        this.sitePanel.add ( new JLabel ( "                                                    " ) );

        this.sitePanel.add ( new JLabel ( "                                                    " ) );


        this.sitePanel.add ( new JLabel ( "-----------------------------------------------------------Mail Details ------------------------------------------------------" ) );

        this.sitePanel.add ( new JLabel ( "                                                                                                                            " ) );

        this.sitePanel.add ( new JLabel ( "                                   " ) );

        this.sitePanel.add ( new JLabel ( "                 " ) );

        this.sitePanel.add ( new JLabel ( " Enter Recipient Mail Id" ) );
        this.sitePanel.add ( this.recipient );
        this.sitePanel.add ( this.addRecipient );
        addRecipient.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ce) {
                if (isValid ( recipient.getText () )) {
                    recipients.add ( recipient.getText () );
                    JOptionPane.showMessageDialog ( frame, recipient.getText () + "  recipient added successfully" );
                    recipient.setText ( "enter mail id" );

                } else {
                    JOptionPane.showMessageDialog ( frame, "Enter Proper MailId" );
                    recipient.setText ( "" );
                }
            }
        } );
        this.sitePanel.add ( new JLabel ( "                           " ) );

        this.sitePanel.add ( new JLabel ( "             " ) );

        this.sitePanel.add ( this.check );
        this.sitePanel.add ( cancel );
        cancel.addActionListener ( new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent ce) {
                dispose ();
                new SiteChecker ();
            }
        } );
        super.add ( this.sitePanel );

    }


    public void actionPerformed(ActionEvent ae) {
        JOptionPane.showMessageDialog ( frame, "recipients size" + recipients.size () );
        if (ae.getSource () == this.check) {

            String url;
            for (int j = 0; j < urls.size (); j++) {
                url = urls.get ( j );
                for (int i = 0; i < recipients.size (); i++) {

                    try {
                        urlCheck ( url, this.time.getText (), recipients );
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }

                }
            }
            super.dispose ();
            new SiteChecker ();
        }
    }

    public void urlCheck(String url, String time, List<String> recipient) throws InterruptedException {

        int minute = Integer.parseInt ( time );
        try {
            URL obj = new URL ( url );
            URLConnection conn = obj.openConnection ();
            String server = conn.getHeaderField ( "Server" );
            for (int i = 1; i <= 3; i++) {
                if (server == null) {
                    timeSchedule ( minute );
                    System.out.println ( " 'Server' is down " );
                    sendMail ( recipient );
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

//        SchedulerFactory sf = new StdSchedulerFactory ();
//
//        Scheduler sched = sf.getScheduler();
//
//        JobDetail job1 = JobBuilder.newJob(DisplayCurrentTime.class)
//
//                .withIdentity("currentTime-Job-1", "group1")
//
//                .build();
//
//
//
//
//
//        Trigger trigger1 = TriggerBuilder.newTrigger()
//
//                .withIdentity("everyMinuteTrigger", "group1")
//
//                .startAt(new Date (System.currentTimeMillis()))
//
//                .withSchedule( CronScheduleBuilder.cronSchedule( "0 0/1 * 1/1 * ? *"))
//
//                .build();
//
//
//
//        Date ft = sched.scheduleJob(job1, trigger1);
//
//


    }

    public void sendMail(final List<String> rep) throws MessagingException {

        List<String> recepient = rep;
        final String mailId = "auxodev1234@gmail.com";
        final String passWord = "auxo1234";
        Properties properties = new Properties ();
        properties.put ( "mail.smtp.starttls.enable", "true" );
        properties.put ( "mail.smtp.host", "smtp.gmail.com" );
        properties.put ( "mail.smtp.port", "587" );
        properties.setProperty ( "mail.smtp.user", mailId );
        properties.setProperty ( "mail.smtp.password", passWord );
        properties.setProperty ( "mail.smtp.auth", "true" );
        Session session = Session.getInstance ( properties, new Authenticator () {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println ( "Email PassWord Authenticate Successfully" );
                return new PasswordAuthentication ( mailId, passWord );
            }
        } );
        Message message = prepareMessage ( session, mailId, recepient );
        Transport.send ( message );
        JOptionPane.showMessageDialog ( frame, "Message Sent Successfully !!!!" );
    }

    private Message prepareMessage(Session session, String myAccountEmail, List<String> recipients) {

        String url = this.url.getText ();
        try {
            Message message = new MimeMessage ( session );
            message.setFrom ( new InternetAddress ( myAccountEmail ) );
            for (int i = 0; i < recipients.size (); i++) {
                message.setRecipient ( Message.RecipientType.TO, new InternetAddress ( recipients.get ( i ) ) );
                message.setSubject ( "Site Checker" );
                message.setText ( url + " url is currently down" );
                return message;
            }
        } catch (MessagingException e) {
            e.printStackTrace ();
        }
        return null;
    }


    public boolean isValid(String checkField) {
        boolean result = false;
        Pattern patternS = Pattern.compile ( "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                "([).!';/?:,][[:blank:]])?$" );
        Matcher ms = patternS.matcher ( checkField );
        boolean matchSite = ms.matches ();
        Pattern patternM = Pattern.compile ( "^(.+)@(.+)$" );
        Matcher mss = patternM.matcher ( checkField );
        boolean matchMailID = mss.matches ();
        if (matchSite || matchMailID) {
            result = true;
        }
        return result;
    }


}