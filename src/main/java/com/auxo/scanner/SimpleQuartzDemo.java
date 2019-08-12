package com.auxo.scanner;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleQuartzDemo {

    /**
     * @param args
     */
    public static void main(String[] args) throws ParseException, SchedulerException {
        SimpleQuartzDemo obj = new SimpleQuartzDemo();
        obj.runDemo();
    }

    public void runDemo() throws SchedulerException, ParseException {

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        /**
         * Job 1 using Trigger
         */
        JobDetail timm = JobBuilder.newJob(DisplayCurrentTime.class)
                .withIdentity("currentTime-Job-1", "group1")
                .build();

        //This trigger will run every minute in infinite loop
        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("everyMinuteTrigger", "group1")
                .startAt(new Date(System.currentTimeMillis()))
                .withSchedule( CronScheduleBuilder.cronSchedule( " 0 2 0,16 8 AUG ? *"))
                .build();

        Date ft = sched.scheduleJob(timm, trigger1);
        sched.start();

        System.out.println(timm.getKey() + " has been scheduled to run at 1 min: " + ft);

        /**
         * Job 2 using SimpleTrigger
         */
        JobDetail job2 = JobBuilder.newJob(DisplayCurrentTime.class)
                .withIdentity("currentTime-Job-2", "group1")
                .build();

        // get a "nice round" time a few seconds in the future....
        Date startTime = DateBuilder.nextGivenSecondDate(null, 10);

        //This trigger will run every 10 sec for 4 times
        SimpleTrigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("fourTimesTrigger", "group1")
                .startAt(startTime)
                .withSchedule( SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .withRepeatCount(4))
                .build();

        ft = sched.scheduleJob(job2, trigger2);
        sched.start();

        System.out.println(timm.getKey() + " has been scheduled to run at: " + ft);

//        /**
//         * Job 3 Using CronTrigger
//         */
//        JobDetail job3 = JobBuilder.newJob(DisplayCurrentTime.class)
//                .withIdentity("currentTime-Job-3", "newGroup")
//                .build();
//
//        //run every 20 seconds
//        CronTrigger trigger3 = TriggerBuilder.newTrigger()
//                .withIdentity("twentySec", "group2")
//                .withSchedule( CronScheduleBuilder.cronSchedule( "0/20 * * * * ?"))
//                .build();
//
//        ft = sched.scheduleJob(job3, trigger3);
//        sched.start();

    }
}

