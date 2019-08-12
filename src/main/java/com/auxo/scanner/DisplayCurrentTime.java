package com.auxo.scanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DisplayCurrentTime implements Job {
    Calendar c;
    Date d;
    SimpleDateFormat sdf;

    public DisplayCurrentTime()
    {}

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        c = new GregorianCalendar();
        d = c.getTime();
        sdf = new SimpleDateFormat("d MMMMM yyyy - HH:mm:ss aaa");
        String msg = String.format("Job Name - %s, Current Time - %s", context.getJobDetail().getKey(), sdf.format(d));
        System.out.println(msg);
    }
}