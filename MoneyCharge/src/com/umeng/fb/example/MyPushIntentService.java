package com.umeng.fb.example;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.umeng.fb.ConversationActivity;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;




public class MyPushIntentService extends UmengBaseIntentService{
	private static final String TAG = MyPushIntentService.class.getName();

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
        FeedbackPush.getInstance(context).init(ConversationActivity.class,true);
        if(FeedbackPush.getInstance(context).onFBMessage(intent)){
            //The push message is reply from developer.
            return;
        }

        //The push message is not reply from developer.
        /*************** other code ***************/

	}
}
