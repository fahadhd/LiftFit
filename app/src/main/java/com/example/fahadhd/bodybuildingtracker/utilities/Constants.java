package com.example.fahadhd.bodybuildingtracker.utilities;

public class Constants {


    public interface ACTION {
        public static String MAIN_ACTION = "com.fahadhd.foregroundservice.action.main";
        public static final String STARTFOREGROUND_ACTION = "com.fahadhd.foregroundservice.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "com.fahadhd.foregroundservice.action.stopforeground";
        public static final String BROADCAST_ACTION = "com.fahadhd.foregroundservice.action.broadcast";
        public static final String BROADCAST_ACTION_OFF = "com.fahadhd.foregroundservice.action.broadcast_off";
    }
    public interface TIMER {
        public static final String CURRENT_TIME = "com.fahadhd.foregroundservice.timer.current_time";
        public static final String DURATION = "com.fahadhd.foregroundservice.timer.duration";
        public static final String TIMER_ID = "com.fahadhd.foregroundservice.timer.timer_id";
        public static final String TIMER_MSG = "com.fahadhd.foregroundservice.timer.timer_msg";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 1;
    }
}