package th.co.logwatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

// get current day .txt file and set to TextView
public class ReadLogActivity extends AppCompatActivity {

    TextView tv_showLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readlog);

        tv_showLog = findViewById(R.id.tv_showLog);

        Intent intent = getIntent();
        String showLog = intent.getStringExtra("showLog");

        tv_showLog.setText(showLog);
    }
}
