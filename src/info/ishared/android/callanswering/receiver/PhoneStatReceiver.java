package info.ishared.android.callanswering.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import info.ishared.android.callanswering.AppConfig;
import info.ishared.android.callanswering.util.LogUtils;
import info.ishared.android.callanswering.util.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;


public class PhoneStatReceiver extends BroadcastReceiver {
    private static String incomingNumber;

    private AudioManager mAudioManager;
    private ITelephony mITelephony;

    File audioFile;
    MediaRecorder mediaRecorder;
    private boolean isRecorder = false;


    @Override
    public void onReceive(Context context, Intent intent) {

        mediaRecorder = new MediaRecorder();

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //利用反射获取隐藏的endcall方法
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            mITelephony = (ITelephony) getITelephonyMethod.invoke(mTelephonyManager, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.log(intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            LogUtils.log("aaaaa.....");
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        } else {
            LogUtils.log("starting.....");
            //如果是来电
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING://来电响铃
                    incomingNumber = intent.getStringExtra("incoming_number");
                    ToastUtils.showMessage(context, incomingNumber + "来电");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://摘机
                    LogUtils.log(tm.getCallState()+"");
                    try {
                        recordCallComment();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE://挂机
                    if (mediaRecorder != null || isRecorder) {
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        isRecorder= false;
                    }
                    ToastUtils.showMessage(context, "挂机了..");
                    break;
            }
        }
    }


    public void recordCallComment() throws IOException {
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        System.out.println(mediaRecorder);
        audioFile = new File(AppConfig.SAVE_DIR + "test.amr");
        //这里AudioSource.MIC可以改为AudioSource.VOICE_CALL, 把音源变
        //电话通话内容, 但似乎很多机都不支持通话录音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        mediaRecorder.prepare();
        mediaRecorder.start();

        isRecorder =true;
    }


    private void endCallAndLogTheNumber(Context context, String incomingNumber) {
        //先静音处理
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        try {
            mITelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

}
