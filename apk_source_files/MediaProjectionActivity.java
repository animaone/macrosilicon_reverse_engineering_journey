package com.p004ms.ms2160.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.p004ms.ms2160.C0443R;
import com.p004ms.ms2160.service.ShotApplication;

/* renamed from: com.ms.ms2160.myapplication.MediaProjectionActivity */
public class MediaProjectionActivity extends Activity {
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 789;
    private String TAG = "MediaProjectionActivity";
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    Messenger mMessenger = null;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 789) {
            Log.e(this.TAG, "Unknown request code: " + requestCode);
        } else if (resultCode != -1) {
            Log.e(this.TAG, "Screen Cast Permission Denied");
        } else {
            ((ShotApplication) getApplication()).setResult(resultCode);
            ((ShotApplication) getApplication()).setIntent(data);
            Message message = new Message();
            message.what = 1;
            try {
                this.mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mMessenger = (Messenger) getIntent().getExtras().get("messenger");
        setContentView(C0443R.layout.activity_mediaprojection);
        this.mMediaProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
        startActivityForResult(this.mMediaProjectionManager.createScreenCaptureIntent(), 789);
    }
}
