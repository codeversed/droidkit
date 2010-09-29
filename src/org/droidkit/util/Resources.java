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

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Set of methods that will access the resources for the application using the library.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class Resources {

    public static final int TYPE_STRING = 1;
    public static final int TYPE_LAYOUT = 2;
    public static final int TYPE_DRAWABLE = 3;
    public static final int TYPE_ID = 4;
    
    /**
     * Returns the integer id for a given view from the application's R class.
     * 
     * @param context The context object used to retrieve the package name.
     * @param id The String name used for the id in the application's R class.
     * @return The integer id used throughout the Android SDK for the view object.
     * @since 1
     */
    public static int findViewId(Context context, String id) {
        String packageName = context.getApplicationInfo().packageName;
        int resId = -1;
        
        try {
            Class<?> res = Class.forName(packageName + ".R$id");
            Field field = res.getField(id);
            
            resId = field.getInt(null);
        } catch (Exception e) {
            Log.e("DroidKit", "Exception thrown retrieving R.id." + id);
        }
        
        return resId;
    }
    
    /**
     * 
     * @param context The context object used to retrieve the package name.
     * @param id The String name used for the layout in the application's R class.
     * @return The integer id used throughout the Android SDK for the layout file.
     * @since 1
     */
    public static int findLayoutId(Context context, String id) {
        String packageName = context.getApplicationInfo().packageName;
        int resId = -1;
        
        try {
            Class<?> res = Class.forName(packageName + ".R$layout");
            Field field = res.getField(id);
            
            resId = field.getInt(null);
        } catch (Exception e) {
            Log.e("DroidKit", "Exception thrown retrieving R.layout." + id);
        }
        
        return resId;
    }
    
    public static int getId(Context context, String id, int type) {
        String packageName = context.getApplicationInfo().packageName;
        int resId = -1;
        Class<?> res = null;
        
        try {
            switch (type) {
            case TYPE_STRING:
                res = Class.forName(packageName + ".R$string");
                break;
            case TYPE_LAYOUT:
                res = Class.forName(packageName + ".R$layout");
                break;
            case TYPE_DRAWABLE:
                res = Class.forName(packageName + ".R$drawable");
                break;
            case TYPE_ID:
                res = Class.forName(packageName + ".R$id");
                break;
            }
            
            Field field = res.getField(id);
            resId = field.getInt(null);
        } catch (Exception e) {
            Log.e("DroidKit", "Error parsing the Class type.");
        }
        
        return resId;
    }
}
