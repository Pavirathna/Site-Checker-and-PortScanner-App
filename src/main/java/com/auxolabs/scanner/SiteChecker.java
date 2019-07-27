package com.auxolabs.scanner;

import java.net.URL;
import java.net.URLConnection;

public class SiteChecker extends TimeScheduler {

    public static void main(String[] args) throws InterruptedException {
        timeSchedule ();

    }

    public static void siteCheck() {
        try {
            // System.out.println ("eg:http://google.com\n Enter:" );
            //String site=sc.next ();
            //  URL obj = new URL(site);
            URL obj = new URL ( "http://tamilrockers.co.com/" );
            URLConnection conn = obj.openConnection ();
            String server = conn.getHeaderField ( "Server" );
            if (server == null) {
                System.out.println ( "Key 'Server' is not found!" );
                SendMail sm = new SendMail ();
                sm.sendMail ();
            } else {
                System.out.println ( "Server - " + server );
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

}


