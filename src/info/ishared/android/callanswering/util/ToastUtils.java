package info.ishared.android.callanswering.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    private static Handler mHandler = new Handler();

    public static void showMessage(final Context context, final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int duration = Toast.LENGTH_SHORT;
                CharSequence sc = msg;
                Toast toast = Toast.makeText(context, sc, duration);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });

    }
}
