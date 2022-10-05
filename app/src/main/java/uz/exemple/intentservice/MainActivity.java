package uz.exemple.intentservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final private static String TAG = "Int";
    private TextView mInfoTextView;
    private ProgressBar mProgressBar;

    private MyBroadcastReceiver mMyBroadcastReceiver;
    private UpdateBroadcastReceiver mUpdateBroadcastReceiver;

    private Intent mMyServiceIntent;
    private int mNumberOfIntentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "=========== onCreate ===========");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }
    void initViews(){
        mInfoTextView = findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        Button startButton = (Button) findViewById(R.id.buttonStart);
        Button stopButton = (Button) findViewById(R.id.buttonStop);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mNumberOfIntentService++;

                // Запускаем свой IntentService
                mMyServiceIntent = new Intent(MainActivity.this, MyIntentService.class);

                startService(mMyServiceIntent.putExtra("time", 5).putExtra("task",
                        "Moshinani zavad bo'lyapti"));
                startService(mMyServiceIntent.putExtra("time", 10).putExtra("task",
                        "Moshinani qizdirilyapti"));
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMyServiceIntent != null) {
                    stopService(mMyServiceIntent);
                    mMyServiceIntent = null;
                }
            }
        });

        mNumberOfIntentService = 0;


        mMyBroadcastReceiver = new MyBroadcastReceiver();
        mUpdateBroadcastReceiver = new UpdateBroadcastReceiver();

        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(
                MyIntentService.ACTION_MYINTENTSERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);

        // Регистрируем второй приёмник
        IntentFilter updateIntentFilter = new IntentFilter(
                MyIntentService.ACTION_UPDATE);
        updateIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mUpdateBroadcastReceiver, updateIntentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyBroadcastReceiver);
        unregisterReceiver(mUpdateBroadcastReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent
                    .getStringExtra(MyIntentService.EXTRA_KEY_OUT);
            mInfoTextView.setText(result);
        }
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int update = intent
                    .getIntExtra(MyIntentService.EXTRA_KEY_UPDATE, 0);
            mProgressBar.setProgress(update);
        }
    }
}