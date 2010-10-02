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
package org.droidkit.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an easy API to creating separated lists for Android user interfaces.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class SeparatedListAdapter extends BaseAdapter {

    private Map<String, BaseAdapter> mSections;
    private ListHeaderAdapter mHeaderAdapter;
    
    public SeparatedListAdapter(Context context) {
        mHeaderAdapter = new ListHeaderAdapter(context);
        mSections = new HashMap<String, BaseAdapter>();
    }
    
    public void addSection(String section, BaseAdapter adapter) {
        mHeaderAdapter.add(section);
        mSections.put(section, adapter);
    }
    
    /**
     * 
     * @since 1
     */
    public void clearItems() {
        mHeaderAdapter.clear();
        mSections.clear();
        
        notifyDataSetChanged();
    }
    
    /**
     * 
     * @return The total number of items in the list, including headers.
     * @since 1
     */
    public int getCount() {
        int total = 0;
        
        for (BaseAdapter adapter : mSections.values()) {
            total += adapter.getCount() + 1;
        }
        
        return total;
    }

    /**
     * {@inheritDoc}
     */
    public Object getItem(int pos) {
        for (Object section : mSections.keySet()) {
            BaseAdapter adapter = mSections.get(section);
            int size = adapter.getCount() + 1;
            
            if (pos == 0) {
                return section;
            }
            
            if (pos < size) {
                return adapter.getItem(pos - 1);
            }
            
            pos -= size;
        }
        
        return null;
    }

    public long getItemId(int pos) {
        return pos;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int pos, View convertView, ViewGroup parent) {
        int sectionNum = 0;
        
        for (Object section : mSections.keySet()) {
            BaseAdapter adapter = mSections.get(section);
            int size = adapter.getCount() + 1;
            
            if (pos == 0) {
                return mHeaderAdapter.getView(sectionNum, null, parent);
            }
            
            if (pos < size) {
                return adapter.getView(pos - 1, null, parent);
            }
            
            pos -= size;
            sectionNum++;
        }
        
        return null;
    }

}
