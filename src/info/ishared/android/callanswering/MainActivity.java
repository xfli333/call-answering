package info.ishared.android.callanswering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import info.ishared.android.callanswering.service.AnswerService;
import info.ishared.android.callanswering.util.SystemUtils;
import info.ishared.android.callanswering.util.ViewUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button mRunServiceBtn;
    private Button mStopServiceBtn;
    private TextView mStatusTextView;

    private Intent serviceIntent ;

    private boolean isServiceWorked = false;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        serviceIntent = new Intent(this, AnswerService.class);
        File file = new File(AppConfig.SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        mHandler = new Handler();

        initGUIComponent();
    }

    private void initGUIComponent() {
        mRunServiceBtn = (Button) this.findViewById(R.id.start_service_btn);
        mStopServiceBtn = (Button) this.findViewById(R.id.stop_service_btn);
        mStatusTextView = (TextView) this.findViewById(R.id.status_txt);

        mRunServiceBtn.setOnClickListener(this);
        mStopServiceBtn.setOnClickListener(this);

        isServiceWorked = SystemUtils.isServiceWorked(this, AppConfig.SERVER_INTENT);
        refreshButtonAndTextView(isServiceWorked);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_service_btn:
                startServiceAndRefreshUI();
                break;
            case R.id.stop_service_btn:
                stopServiceAndRefreshUI();
                break;
        }
    }

    private void startServiceAndRefreshUI() {
        this.startService(serviceIntent);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                while (!SystemUtils.isServiceWorked(MainActivity.this, AppConfig.SERVER_INTENT)) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                refreshButtonAndTextView(true);
            }
        });
    }

    private void stopServiceAndRefreshUI() {
        this.stopService(serviceIntent);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                while (SystemUtils.isServiceWorked(MainActivity.this, AppConfig.SERVER_INTENT)) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                refreshButtonAndTextView(false);
            }
        });
    }

    private void refreshButtonAndTextView(boolean isServiceWorked) {
        if (isServiceWorked) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mStatusTextView.setText("来电答录服务正在运行....");
                    ViewUtils.hideView(mRunServiceBtn);
                    ViewUtils.showView(mStopServiceBtn);
                }
            });

        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mStatusTextView.setText("来电答录服务已经停止....");
                    ViewUtils.hideView(mStopServiceBtn);
                    ViewUtils.showView(mRunServiceBtn);
                }
            });

        }
    }


}
