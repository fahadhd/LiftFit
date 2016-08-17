package com.example.fahadhd.bodybuildingtracker.utilities;

public class Constants {


    public interface ACTION {
        public static final String START_NOTIFICATION_ACTION = "com.fahadhd.foregroundservice.action.start_notification";
        public static final String START_FOREGROUND_ACTION = "com.fahadhd.foregroundservice.action.start_foreground";
        public static final String STOP_FOREGROUND_ACTION = "com.fahadhd.foregroundservice.action.stop_foreground";

    }
    public interface TIMER {
        public static final String TIMER_RUNNING = "com.fahadhd.foregroundservice.action.timer_on";
        public static final String TIMER_OFF = "com.fahadhd.foregroundservice.action.timer_off";
        public static final String TIMER_MSG = "com.fahadhd.foregroundservice.general.timerMsg";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 1;
    }
    public interface  GENERAL{
        public static final String SESSION_ID = "com.fahadhd.foregroundservice.general.sessionID";
    }
}