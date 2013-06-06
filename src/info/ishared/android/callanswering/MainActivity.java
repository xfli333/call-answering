package info.ishared.android.callanswering;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import info.ishared.android.callanswering.service.AnswerService;
import info.ishared.android.callanswering.util.ViewUtils;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button mRunServiceBtn;
    private Button mStopServiceBtn;

    private Intent serviceIntent = new Intent(this, AnswerService.class);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        File file=new File(AppConfig.SAVE_DIR);
        if(!file.exists()){
            file.mkdirs();
        }

        initGUIComponent();
    }

    private void initGUIComponent(){
        mRunServiceBtn=(Button)this.findViewById(R.id.start_service_btn);
        mStopServiceBtn=(Button)this.findViewById(R.id.stop_service_btn);

        mRunServiceBtn.setOnClickListener(this);
        mStopServiceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_service_btn:
                ViewUtils.hideView(mRunServiceBtn);
                ViewUtils.showView(mStopServiceBtn);
                this.startService(serviceIntent);
                break;
            case R.id.stop_service_btn:
                ViewUtils.hideView(mStopServiceBtn);
                ViewUtils.showView(mRunServiceBtn);
                this.stopService(serviceIntent);
                break;
        }
    }
}
