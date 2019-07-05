package th.co.logwatcher;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }

        final Alarm alarm = new Alarm();
        alarm.setAlarm(MainActivity.this);
        //setAlarm();

        findViewById(R.id.bt_getLog).setOnClickListener(this);

        findViewById(R.id.bt_appcrash).setOnClickListener(this);

        findViewById(R.id.bt_logErr).setOnClickListener(this);

        findViewById(R.id.bt_logDebug).setOnClickListener(this);

        findViewById(R.id.bt_logInform).setOnClickListener(this);

        findViewById(R.id.bt_logVerbose).setOnClickListener(this);

        findViewById(R.id.bt_logWarn).setOnClickListener(this);

        findViewById(R.id.bt_assert).setOnClickListener(this);

        findViewById(R.id.bt_showLog).setOnClickListener(this);

        findViewById(R.id.bt_sendEmail).setOnClickListener(this);

    }

    // print log
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_getLog:
                new GetLogAsyncTask(MainActivity.this).execute();
                break;
            case R.id.bt_appcrash:
                crashApp();
                break;
            case R.id.bt_logErr:
                Log.e(Constants.LOG_TAG, getResources().getString(R.string.log_err_msg));
                break;
            case R.id.bt_logDebug:
                Log.d(Constants.LOG_TAG, getResources().getString(R.string.log_debug_msg));
                break;
            case R.id.bt_logInform:
                Log.i(Constants.LOG_TAG, getResources().getString(R.string.log_informative_msg));
                break;
            case R.id.bt_logVerbose:
                Log.v(Constants.LOG_TAG, getResources().getString(R.string.log_verbose_msg));
                break;
            case R.id.bt_logWarn:
                Log.w(Constants.LOG_TAG, getResources().getString(R.string.log_warning_msg));
                break;
            case R.id.bt_assert:
                Log.wtf(Constants.LOG_TAG, getResources().getString(R.string.log_assert_msg));
                break;
            case R.id.bt_showLog:
                String showLog = FileHelper.ReadFile(MainActivity.this);
                Intent intent = new Intent(MainActivity.this , ReadLogActivity.class);
                intent.putExtra("showLog" , showLog);
                startActivity(intent);
                break;
            case R.id.bt_sendEmail:
                try {
                    FileHelper.zip(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new SendEmailAsyncTask(MainActivity.this).execute();
                break;
        }
    }

    // Make app crash by IndexOutOfBoundsException
    private void crashApp(){
        ArrayList<String> arrayList = new ArrayList<>();
        String getarr = arrayList.get(0);
    }

    // save log to .txt before Activity in onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf(Constants.LOG_TAG , "ON DESTROY");
        new GetLogAsyncTask(MainActivity.this);
    }

}
