package com.DevLibrary.Observer;
import com.DevLibrary.Logger.ActivityLogger;

public class ActivityLoggerObserver implements ResourceObserver {

    @Override
    public void update(String message) {
        //Create and start the background Thread for logging
        ActivityLogger activityLogger = new ActivityLogger(message);
        Thread thread = new Thread(activityLogger);
        thread.start();
    }
}