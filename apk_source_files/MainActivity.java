package com.p004ms.ms2160.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.p004ms.ms2160.C0443R;
import com.p004ms.ms2160.service.CaptureService;

/* renamed from: com.ms.ms2160.myapplication.MainActivity */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private final String ACTION_USB_PERMISSION = "com.ms.ms2160.USB_PERMISSION";
    private final String ACTION_USB_REFRESH = "com.ms.ms2160.USB_REFRESH";
    private String TAG = "MainActivity";
    Handler mAsyncHandler = null;
    Runnable sendBroadcastRunnable = new Runnable() {
        public void run() {
            MainActivity.this.sendBroadcast(new Intent("com.ms.ms2160.USB_REFRESH"));
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0443R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(C0443R.C0445id.toolbar));
        Log.i(this.TAG, "MainActivity onCreate");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        String action = getIntent().getAction();
        Log.e("MainActivity", "onResume: " + action);
        if (action == null || action.equals("android.intent.action.MAIN")) {
            getApplicationContext().startService(new Intent(this, CaptureService.class));
        } else if (action.equals(USBMonitor.ACTION_USB_DEVICE_ATTACHED)) {
            Log.e(this.TAG, USBMonitor.ACTION_USB_DEVICE_ATTACHED);
            if (this.mAsyncHandler == null) {
                this.mAsyncHandler = HandlerThreadHandler.createHandler(this.TAG);
            }
            getApplicationContext().startService(new Intent(this, CaptureService.class));
            this.mAsyncHandler.postDelayed(this.sendBroadcastRunnable, 1000);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0443R.C0446menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0443R.C0445id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case C0443R.C0445id.menu_detect_devices:
                sendBroadcast(new Intent("com.ms.ms2160.USB_REFRESH"));
                Log.e("MainActivity", "sendBroadcast");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") + ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") == 0) {
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE") || ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECORD_AUDIO")) {
            Snackbar.make(findViewById(C0443R.C0445id.layout), (CharSequence) "Please Grant Permissions", -2).setAction((CharSequence) "ENABLE", (View.OnClickListener) new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"}, MainActivity.PERMISSIONS_MULTIPLE_REQUEST);
                }
            }).show();
            return;
        }
        requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"}, PERMISSIONS_MULTIPLE_REQUEST);
    }

    private void startScreenCapture() {
        getApplicationContext().startService(new Intent(this, CaptureService.class));
    }
}
