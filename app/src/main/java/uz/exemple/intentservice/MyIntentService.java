package uz.exemple.intentservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class MyIntentService extends IntentService {
    private final String TAG = "IntentServiceLogs";

    public static final String ACTION_MYINTENTSERVICE = "RESPONSE";
    public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    String extraOut = "Moshina zavad bo'ldi, qizdirildi haydashga tayyor!";

    public static final String ACTION_UPDATE = "UPDATE";
    public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";

    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    private boolean mIsSuccess;
    private boolean mIsStopped;


    public MyIntentService() {
        super("my_intentService");
        mIsSuccess = false;
        mIsStopped = false;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public void onDestroy() {
        String notice;

        mIsStopped = true;

        if (mIsSuccess) {
            notice = "onDestroy with success";

        } else {
            notice = "onDestroy WITHOUT success!";

        }

        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_LONG)
                .show();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_MYINTENTSERVICE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(EXTRA_KEY_OUT, extraOut);
        sendBroadcast(responseIntent);
        super.onDestroy();

    }
    @Override
    protected void onHandleIntent(Intent intent) {

        int tm = intent.getIntExtra("time", 0);
        String label = intent.getStringExtra("task");
        Toast.makeText(getApplicationContext(),label,Toast.LENGTH_LONG).show();
        Log.d(TAG, "onHandleIntent start: " + label);

        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_MYINTENTSERVICE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(EXTRA_KEY_OUT, label);
        sendBroadcast(responseIntent);
        for (int i = 0; i <= tm; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(mIsStopped){
                break;
            }

            Intent updateIntent = new Intent();
            updateIntent.setAction(ACTION_UPDATE);
            updateIntent.addCategory(Intent.CATEGORY_DEFAULT);
            updateIntent.putExtra(EXTRA_KEY_UPDATE, i);
            sendBroadcast(updateIntent);

            mIsSuccess = true;

            String notificationText = String.valueOf((100 * i / 10)) + " %";
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Progress")
                    .setContentText(notificationText)
                    .setTicker("Notification!")
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }

        Log.d(TAG, "onHandleIntent end: " + label);
    }
}
