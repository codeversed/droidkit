/*
 * Copyright (C) 2010-2011 Michael Novak <michael.novakjr@gmail.com>
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabItem extends RelativeLayout {
    ImageView mIcon;
    TextView mLabel;
    
    String mTag;
    
    int mSelectedTextColor = 0x242424;
    int mUnselectedTextColor = 0xFFFFFF;
    
    public TabItem(Context context) {
        super(context);
        init();
    }
    
    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        mIcon = new ImageView(getContext());
        mLabel = new TextView(getContext());
        
        LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iconParams.addRule(CENTER_HORIZONTAL, TRUE);
        
        mIcon.setPadding(0, 10, 0, 0);
        addView(mIcon, iconParams);
        
        LayoutParams labelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labelParams.addRule(CENTER_HORIZONTAL, TRUE);
        labelParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        
        mLabel.setPadding(0, 0, 0, 6);
        addView(mLabel, labelParams);
    }
    
    public void setTag(String tag) {
        mTag = tag;
    }
    
    public String getTag() {
        return mTag;
    }
    
    public void setTypeface(Typeface tf) {
        mLabel.setTypeface(tf);
    }
    
    public void setText(String text) {
        mLabel.setText(text);
    }
    
    public void setSelectedTextColor(String color) {
        mSelectedTextColor = Color.parseColor(color);
    }
    
    public void setSelectedTextColor(int color) {
        mSelectedTextColor = color;
    }
    
    public void setUnselectedTextColor(String color) {
        mUnselectedTextColor = Color.parseColor(color);
    }
    
    public void setUnselectedTextColor(int color) {
        mUnselectedTextColor = color;
    }
    
    public void setIcon(int id) {
        mIcon.setImageResource(id);
    }
    
    public void setIcon(Drawable img) {
        mIcon.setImageDrawable(img);
    }
    
    public void setBackground(int id) {
        setBackgroundResource(id);
    }
    
    public void setTabSelected(boolean selected) {
        if (selected) {
            setSelected(true);
            mLabel.setTextColor(mSelectedTextColor);
        } else {
            setSelected(false);
            mLabel.setTextColor(mUnselectedTextColor);
        }
    }
}
