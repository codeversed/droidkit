/*
 * Copyright (C) 2010 Mike Novak <michael.novakjr@gmail.com>
 * Copyright (C) 2007 The Android Open Source Project
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
package org.droidkit.demos;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DroidkitDemos extends ListActivity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        String path = intent.getStringExtra("org.droidkit.demos.Path");
        
        if (path == null) {
            path = "";
        }
        
        setListAdapter(new SimpleAdapter(this, getData(path), android.R.layout.simple_list_item_1, 
                new String[] { "title" }, new int[] { android.R.id.text1 }));
        getListView().setTextFilterEnabled(true);
    }
    
    protected List getData(String prefix) {
        List<Map> data = new ArrayList<Map>();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
        
        if (list == null) {
            return data;
        }
        
        String[] prefixPath;
        
        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
        }
        
        int len = list.size();
        
        Map<String, Boolean> entries = new HashMap<String, Boolean>();
        
        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            
            if (prefix.length() == 0 || label.startsWith(prefix)) {
                String[] labelPath = label.split("/");
                String nextLabel = prefixPath == null ? labelPath[0] : labelPath[prefixPath.length];
                
                if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(data, nextLabel, activityIntent(info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(data, nextLabel, browseIntent(prefix.equals("") ? nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }
        
        Collections.sort(data, sDisplayNameComparator);
        
        return data;
    }
    
    private final static Comparator<Map> sDisplayNameComparator = new Comparator<Map>() {
        private final Collator collator = Collator.getInstance();
        
        public int compare(Map map1, Map map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };
    
    protected void addItem(List<Map> data, String name, Intent intent) {
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("title", name);
        tmp.put("intent", intent);
        data.add(tmp);
    }
    
    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }
    
    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, DroidkitDemos.class);
        result.putExtra("org.droidkit.demos.Path", path);
        return result;
    }
    
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        Map map = (Map) list.getItemAtPosition(position);
        
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
