package info.ishared.android.callanswering.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import info.ishared.android.callanswering.receiver.PhoneStatReceiver;
import info.ishared.android.callanswering.util.LogUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-6-6
 * Time: PM1:06
 */
public class AnswerService extends Service {
    private MyBinder binder = new MyBinder();
    private PhoneStatReceiver phoneStatReceiver;

    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public AnswerService getService() {
            return AnswerService.this;
        }
    }


    @Override
    public void onCreate() {
        LogUtils.log("onCreate....");
        super.onCreate();
        phoneStatReceiver = new PhoneStatReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        //拦截电话
        mIntentFilter.addAction("android.intent.action.PHONE_STATE");

        //拦截短信
//        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(phoneStatReceiver, mIntentFilter);
    }

    @Override
    public void onDestroy() {
        LogUtils.log("onDestroy....");
        unregisterReceiver(phoneStatReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.log("onUnbind....");
        return super.onUnbind(intent);
    }
}
