//package com.androidexample.mobilegcm;
package com.example.abusufian.jobcueandroid.chat;

/**
 * Created by sadaf2605 on 5/3/16.
 */
public interface Config {

    // CONSTANTS

    // When you are using two simulator for testing application.
    // Then Make SECOND_SIMULATOR value true when opening/installing application in second simulator
    // Actually we are validating/saving device data on IMEI basis.
    // if it is true IMEI number change for second simulator

    static final boolean SECOND_SIMULATOR = false;

    // Server Url absolute url where php files are placed.
    static final String YOUR_SERVER_URL   =  "YOUR_SERVER_URL/FOLDER_NAMES_WHERE_SERVER_FILES_PLACED/";

    // Google project id
    static final String GOOGLE_SENDER_ID = "943293855675";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    // Broadcast reciever name to show gcm registration messages on screen
    static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_REGISTRATION_MESSAGE";

    // Broadcast reciever name to show user messages on screen
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_MESSAGE";

    // Parse server message with this name
    static final String EXTRA_MESSAGE = "message";


}