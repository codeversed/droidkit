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

import android.app.LocalActivityManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.droidkit.R;

public class TabWidget extends RelativeLayout {
    private LocalActivityManager mLocalActivityManager;
    private TabBar mTabBar;
    private LinearLayout mContent;
    private ImageView mShadow;
    
    private int mCurrentIndex = -1;
    
    private static final int TAB_BAR_ID = 0x0011;
    private static final int CONTENT_ID = 0x0012;
    
    public TabWidget(Context context) {
        super(context);
    }
    
    public TabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    private void init() {
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        
        mTabBar = new TabBar(getContext());
        mContent = new LinearLayout(getContext());
        mShadow = new ImageView(getContext());
        
        mTabBar.setId(TAB_BAR_ID);
        mContent.setId(CONTENT_ID);
        mShadow.setImageResource(R.drawable.shadow_gradient);
        
        LayoutParams tabBarParams = new LayoutParams(LayoutParams.FILL_PARENT, 64);
        addView(mTabBar, tabBarParams);
        
        LayoutParams contentParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        contentParams.addRule(BELOW, TAB_BAR_ID);
        addView(mContent, contentParams);
        
        LayoutParams shadowParams = new LayoutParams(LayoutParams.FILL_PARENT, 5);
        shadowParams.addRule(ALIGN_TOP, CONTENT_ID);
        addView(mShadow, shadowParams);
    }
    
    public void setSelectedTab(int index) {
        if (mCurrentIndex == index) {
            return;
        }
        
        TabItem tab = mTabBar.getTabs().get(index);
        tab.setSelected(true);
        
        
    }
    
    public void addTab(TabItem item) {
        mTabBar.addTab(item);
    }
    
    public void setup(LocalActivityManager activityGroup) {
        mLocalActivityManager = activityGroup;
    }
}
