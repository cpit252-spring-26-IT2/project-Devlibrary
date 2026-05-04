package com.DevLibrary.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger implements Runnable {
    // The message we want to log
    private String logMessage;
    // The text file name
    private final String filePath = "history_log.txt";

    public ActivityLogger(String logMessage) {
        this.logMessage = logMessage;
    }
//    The run() method executes in the background
    @Override
    public void run() {
// Format the current date and time
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String Message = "[" + timeStamp + "] EVENT: " + logMessage;

        //Using IO Streams to write to the file
        try{
            // BufferedWriter uses a memory buffer before writing text to the file.
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter writer = new BufferedWriter(fw);
            // Write the message into the buffer
            writer.write(Message);
            writer.newLine();

            // Force any text stored in the buffer to be written to the file
            writer.flush();
            writer.close();

        }catch (IOException e) {
            System.out.println("Thread-Log Error writing to file: " + e.getMessage());

        }
    }
}
