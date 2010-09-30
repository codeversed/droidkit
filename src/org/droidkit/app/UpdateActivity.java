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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.droidkit.util.Resources;
import org.droidkit.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class UpdateActivity extends Activity {

    ImageView mIconView;
    TextView mTitleView;
    TextView mAuthorView;
    TextView mPublishedView;
    TextView mVersionView;
    TextView mDownloadLabel;
    WebView mNotesView;
    ProgressBar mProgressUpdate;
    
    Button mCloseButton;
    Button mUpdateButton;
    String mApkFile;
    boolean mServiceRunning = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(Resources.getId(this, "activity_update", Resources.TYPE_LAYOUT));
        
        bindService(new Intent(this, UpdateService.class), mConnection, BIND_AUTO_CREATE);
        mServiceRunning = true;
        
        mIconView = 
            (ImageView) findViewById(Resources.getId(this, "update_act_icon", Resources.TYPE_ID));
        mTitleView = 
            (TextView) findViewById(Resources.getId(this, "update_act_title", Resources.TYPE_ID));
        mAuthorView =
            (TextView) findViewById(Resources.getId(this, "update_act_author", Resources.TYPE_ID));
        mVersionView = 
            (TextView) findViewById(Resources.getId(this, "update_act_ver", Resources.TYPE_ID));
        mDownloadLabel = 
            (TextView) findViewById(Resources.getId(this, "update_act_dl", Resources.TYPE_ID));
        
        mNotesView =
            (WebView) findViewById(Resources.getId(this, "update_act_relnotes", Resources.TYPE_ID));
        
        mIconView.setImageResource(getApplicationInfo().icon);
        
        mProgressUpdate =
            (ProgressBar) findViewById(Resources.getId(this, "update_act_bar", Resources.TYPE_ID));
        
        mCloseButton =
            (Button) findViewById(Resources.getId(this, "update_act_cancel", Resources.TYPE_ID));
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UpdateActivity.this.finish();
            }
        });
        
        mUpdateButton = 
            (Button) findViewById(Resources.getId(this, "update_act_inst", Resources.TYPE_ID));
        mUpdateButton.setOnClickListener(mUpdateClick);
        
        /* pull the json data from the intent. */
        try {
            String obj = getIntent().getStringExtra("json");
            JSONObject json = new JSONObject(obj);
            
            mTitleView.setText(json.getString("title"));
            mAuthorView.setText(json.getString("author"));
            
            /* version specific information. */
            JSONArray updates = json.getJSONArray("updates");
            JSONObject update = updates.getJSONObject(0);
            mNotesView.loadData(update.getString("release.notes"), "text/html", "utf-8");
            mApkFile = update.getString("apk.url");
            mVersionView.setText("Version: " + update.getString("version.string"));
            
            Log.i("DroidKit", "HTML: " + update.getString("release.notes"));
        } catch (JSONException e) {
            Log.e("DroidKit", "Error parsing the update JSON.");
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (mServiceRunning) {
            unbindService(mConnection);
        }
    }
    
    protected ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateService updater = ((UpdateService.UpdateBinder) service).getService();
            boolean isDownloading = updater.isDownloading();
            
            if (isDownloading) {
                mProgressUpdate.setIndeterminate(true);
                mProgressUpdate.setVisibility(View.VISIBLE);
                mUpdateButton.setEnabled(false);
                mDownloadLabel.setText("Downloading update...");
                mDownloadLabel.setVisibility(View.VISIBLE);
                
                updater.setHandler(mHandler);
            }
            
        }
        
        public void onServiceDisconnected(ComponentName name) {
            Log.d("DroidKit", "Service is being disconnected...");
        }
    };
    
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UpdateService.DOWNLOAD_DONE:
                mDownloadLabel.setVisibility(View.GONE);
                mProgressUpdate.setVisibility(View.GONE);
                mUpdateButton.setEnabled(true);
                break;
            }
        }
    };
    
    View.OnClickListener mUpdateClick = new View.OnClickListener() {
        public void onClick(View view) {
            String fileName = Environment.getExternalStorageDirectory() + "/" + mApkFile.substring(mApkFile.lastIndexOf("/") + 1);
            
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
            
            startActivity(intent);
        }
    };
}
