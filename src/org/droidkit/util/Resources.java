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
import android.view.View;

/**
 * Set of methods that will access the resources for the application using the library.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class Resources {

    /**
     * Retrieve a View object based on the R.id from the application resources.
     * 
     * @param context The Context object used to access the application info.
     * @param id The View's id as defined in the R class.
     * @return The View for the given id or null if not found.
     * @since 1
     */
    public static View findViewById(Context context, int id) {
        String packageName = context.getApplicationInfo().packageName;
        
        try {
            Class<?> res = Class.forName(packageName + ".R$id");
            
        } catch (Exception e) {
            
        }
        
        return null;
    }
}
