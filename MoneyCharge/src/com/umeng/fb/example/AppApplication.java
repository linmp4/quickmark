package com.umeng.fb.example;

import android.app.Application;
import com.umeng.fb.push.FeedbackPush;

/**
 * Created by user on 2014/10/13.
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        FeedbackPush.getInstance(this).init(false);
//        AppPush.getInstance(this).init();
    }
}
