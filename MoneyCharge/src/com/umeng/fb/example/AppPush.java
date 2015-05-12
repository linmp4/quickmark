package com.umeng.fb.example;


import android.content.Context;

import com.umeng.fb.push.FBMessage;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

public class AppPush {

    public UmengMessageHandler mMessageHandler;
    private static AppPush mAppPush;
    private Context context;

    public static AppPush getInstance(Context context) {
        if (mAppPush == null)
            mAppPush = new AppPush(context);
        return mAppPush;
    }

    public AppPush(Context context) {
        this.context = context;
    }

    public void init() {
        mMessageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(Context context, UMessage msg) {
                if (FeedbackPush.getInstance(context).dealFBMessage(new FBMessage(msg.custom))) {
                    //The push message is reply from developer.
                    return;
                }

                //The push message is not reply from developer.
                /*************** other code ***************/
            }
        };
        PushAgent.getInstance(context).setMessageHandler(mMessageHandler);
    }
}
