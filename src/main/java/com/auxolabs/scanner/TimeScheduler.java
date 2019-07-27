package com.auxolabs.scanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeScheduler {

    public static void timeSchedule() throws InterruptedException {

        for(int i=0;i<3;i++) {
            TimeUnit.MINUTES.sleep ( 1 );
            DateFormat df = new SimpleDateFormat ( "dd/MM/yy HH:mm:ss" );
            Date dateobj = new Date ();
            System.out.println ( df.format ( dateobj ) );
            SiteChecker sc=new SiteChecker ();
            sc.siteCheck ();

        }
    }

}



