package info.ishared.android.callanswering.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-12-8
 * Time: 下午1:25
 */
public class SystemUtils {
    public static void killSelf() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
