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
package org.droidkit.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.droidkit.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Provides an easy over the air update service for applications that are not distributed via
 * any marketplace. If your application is used in a market of some kind this is not a recommended
 * service.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class UpdateService extends Service {

    public static final int UPDATE_AVAILABLE = 1;
    public static final int UPDATE_SERVICE_ID = 0x011;
    
    private NotificationManager mNotificationManager;
    private IBinder mBinder = new UpdateBinder();
    private String mUpdateServer;
    private SharedPreferences mPreferences;
    
    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        mPreferences = getSharedPreferences("droidkit.prefs", MODE_PRIVATE);
        boolean checkForUpdates = mPreferences.getBoolean("check.updates", true);
        
        if (!checkForUpdates) {
            stopSelf();
            return;
        }
        
        /* pull the url from the strings.xml file of the application. */
        String packageName = getApplicationInfo().packageName;
        
        try {
            Class<?> res = Class.forName(packageName + ".R$string");
            Field field = res.getField("update_service_url");
            int resId = field.getInt(null);
            mUpdateServer = getString(resId);
        } catch (Exception e) {
            Log.e("DroidKit", "Error locating the update service url." + e.toString());
        }
        
        if (mUpdateServer == null) {
            /* abort the process, we don't have an update server to contact. */
            stopSelf();
            return;
        }
        
        Thread thr = new Thread(mRunnable);
        thr.start();
    }
    
    private void queryServer() {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(mUpdateServer);
        StringBuffer obj = new StringBuffer();
        
        try {
            HttpResponse response = client.execute(get);
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in), 
                    (int) response.getEntity().getContentLength());
            
            String line = "";
            
            while ((line = reader.readLine()) != null) {
                obj.append(line);
                obj.append("\n");
            }
        } catch (ClientProtocolException e) {
            Log.e("DroidKit", "Error contacting update server: " + e.toString());
        } catch (IOException e) {
            Log.e("DroidKit", "Error contacting update server: " + e.toString());
        }
        
        client.getConnectionManager().shutdown();
        
        parseJSONResponse(obj.toString());
    }
    
    private void parseJSONResponse(String obj) {
        /* find out the current version of the application.  we use the version code for updates. */
        int currentVersion = 0;
        
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = info.versionCode;
        } catch (NameNotFoundException e) {
            Log.e("DroidKit", "Wtf! How can we not find ourselves! " + e.toString());
        }
        
        try {
            JSONArray arr = new JSONArray(obj);
            JSONObject json = arr.getJSONObject(0);
            
            int updateVersion = json.getInt("version.code");
            
            if (currentVersion < updateVersion) {
                boolean result = updateVersion(json.getString("apk.url"));
                    
                if (result) {
                    notifyUser(obj);
                }
            }
        } catch (JSONException e) {
            Log.e("DroidKit", "Error parsing update server response: " + e.toString());
        }
    }
    
    private boolean updateVersion(String apk) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(apk);
        boolean result = false;
        
        try {
            HttpResponse response = client.execute(get);
            InputStream in = response.getEntity().getContent();
            
            File localFile = new File(Environment.getExternalStorageDirectory()+ "/" + 
                    apk.substring(apk.lastIndexOf("/") + 1));
            FileOutputStream out = new FileOutputStream(localFile);
            int count;
            byte[] buffer = new byte[8196];
            
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            
            out.close();
            in.close();
            result = true;
        } catch (ClientProtocolException e) {
            Log.e("DroidKit", "Error retrieving updated apk: " + e.toString());
        } catch (IOException e) {
            Log.e("DroidKit", "Error retrieving updated apk: " + e.toString());
        }
        
        client.getConnectionManager().shutdown();
        return result;
    }
    
    private void notifyUser(String json) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("json", json);
        
        String desc = "Update available for " + getApplicationInfo().name;
        
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
        Notification n = new Notification(android.R.drawable.stat_sys_download_done, 
                "Update available", System.currentTimeMillis());
        n.setLatestEventInfo(this, "Update Available", desc, pending);
        
        mNotificationManager.notify("Update Available", UPDATE_SERVICE_ID, n);
    }
    
    private Runnable mRunnable = new Runnable() {
        public void run() {
            queryServer();
        }
    };
    
    @Override
    public IBinder onBind(Intent intent) {
        /* just start the service, it actually makes no sense to bind to it. */
        startService(new Intent(this, UpdateService.class));
        return mBinder;
    }
    
    /**
     * 
     * @version 1
     * @since 1
     * @author mrn
     */
    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}
