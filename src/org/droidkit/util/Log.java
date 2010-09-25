/*
 * Copyright 2010 Mike Novak <michael.novakjr@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.droidkit.util;

/**
 * A wrapper for the Android Log class that throttles the types of messages
 * pushed to the console. The goal is to only have error messages print by
 * default but have the ability to turn on more verbose logging in the event
 * something needs to be debugged.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class Log {

    /**
     * Prints a debug message to the Android console log. The message will only
     * print if the log level set on the device is at least at DEBUG for the
     * provided tag.
     * 
     * @param message The message to print to the log.
     * @since 1
     */
    public static void d(String tag, String message) {
        if (android.util.Log.isLoggable(tag, android.util.Log.DEBUG)) {
            android.util.Log.d(tag, message);
        }
    }

    /**
     * Prints an info message to the Android console log. The message will only
     * print if the log level set on the device is at least at INFO for the
     * provided tag.
     * 
     * @param message The message to print to the log.
     * @since 1
     */
    public static void i(String tag, String message) {
        if (android.util.Log.isLoggable(tag, android.util.Log.INFO)) {
            android.util.Log.d(tag, message);
        }
    }

    /**
     * Prints a warning message to the Android console log. The message will
     * only print if the log level set on the device is at least at WARN for the
     * provided tag.
     * 
     * @param message The message to print to the log.
     * @since 1
     */
    public static void w(String tag, String message) {
        if (android.util.Log.isLoggable(tag, android.util.Log.WARN)) {
            android.util.Log.w(tag, message);
        }
    }

    /**
     * Prints an error message to the Android console log. The message will only
     * print if the log level set on the device is at least at ERROR for the
     * provided tag.
     * 
     * @param message The message to print to the log.
     * @since 1
     */
    public static void e(String tag, String message) {
        if (android.util.Log.isLoggable(tag, android.util.Log.ERROR)) {
            android.util.Log.e(tag, message);
        }
    }

    /**
     * Prints a verbose message to the Android console log. The message will
     * only print if the log level set on the device is at least VERBOSE for the
     * provided tag..
     * 
     * @param message The message to print to the log.
     * @since 1
     */
    public static void v(String tag, String message) {
        if (android.util.Log.isLoggable(tag, android.util.Log.VERBOSE)) {
            android.util.Log.v(tag, message);
        }
    }
}
