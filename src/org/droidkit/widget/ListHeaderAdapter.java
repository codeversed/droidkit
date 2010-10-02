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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.droidkit.util.Resources;

import java.util.Vector;

/**
 * Simple adapter used for headers in a {@link SeparatedListAdapter}.
 * 
 * @version 1
 * @since 1
 * @author mrn
 */
public class ListHeaderAdapter extends BaseAdapter {

    private Context mContext;
    private Vector<String> mHeaders;
    
    public ListHeaderAdapter(Context context) {
        mContext = context;
        mHeaders = new Vector<String>();
    }
    
    /**
     * Adds a String to the list of headers for this adapter.
     * 
     * @param header String header title to add to the list.
     * @since 1
     */
    public void add(String header) {
       mHeaders.add(header); 
    }
    
    /**
     * Adds a String from a strings.xml entry.
     * 
     * @param header The R.string id for the requested String.
     * @since 1
     */
    public void add(int header) {
        String str = mContext.getString(header);
        mHeaders.add(str);
    }
    
    /**
     * Clears out all the header titles associated with this adapter.
     * 
     * @since 1
     */
    public void clear() {
        mHeaders.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mHeaders.size();
    }

    /**
     * {@inheritDoc}
     */
    public Object getItem(int pos) {
        return mHeaders.get(pos);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int pos) {
        return pos;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            LayoutInflater inflater = 
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            convertView = inflater.inflate(Resources.findLayoutId(mContext, "list_header"), 
                parent, false);
            
            holder = new ViewHolder();
            holder.headerView = (TextView) convertView.findViewById(Resources.findViewId(mContext,
                "list_header_title"));
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.headerView.setText(mHeaders.get(pos));
        
        return convertView;
    }

    /**
     * Object used to cache the list item views for much better performance, it won't require an
     * inflation at each item.
     * 
     * @version 1
     * @since 1
     * @author mrn
     */
    static class ViewHolder {
        TextView headerView;
    }
}
