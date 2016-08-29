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
        public static final String NEW_WORKOUT_DIALOG = "com.fahadhd.templatetask.new_workout_dialog";
        public static final String SESSION_ID = "com.fahadhd.foregroundservice.general.sessionID";
    }

    public interface WORKOUT_TASK {
        public static final String ADD_WORKOUT = "com.fahadhd.workouttask.add_workout";
        public static final String UPDATE_WORKOUT = "com.fahadhd.workouttask.update_workout";
        public static final String DELETE_WORKOUT = "ccom.fahadhd.workouttask.delete_workout";
    }

    public interface TEMPLATE_TASK {
        public static final String TEMPLATE_NAME = "com.fahadhd.templatetask.template_name";
        public static final String TEMPLATE_EMPTY_KEY = "com.fahadhd.templatetask.is_template_empty";
        public static final String SAVE_TEMPLATE = "com.fahadhd.templatetask.save_template";
        public static final String LOAD_TEMPLATE = "com.fahadhd.templatetask.load_template";
        public static final String CLEAR_TEMPLATE = "com.fahadhd.templatetask.clear_template";
    }
}