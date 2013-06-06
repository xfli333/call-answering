package info.ishared.android.callanswering;

import android.os.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-5-7
 * Time: AM10:46
 */
public class AppConfig {
    public static final String TAG = "Call Answering";
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    public static final String SAVE_DIR = SD_PATH+"call_answering/";
}
