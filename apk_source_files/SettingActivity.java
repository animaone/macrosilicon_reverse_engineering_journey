package com.p004ms.ms2160.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.p004ms.ms2160.C0443R;
import com.p004ms.ms2160.CSharpTool.C0442Util;
import com.p004ms.ms2160.service.CaptureService;
import com.p004ms.ms2160.service.ShotApplication;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.ms.ms2160.myapplication.SettingActivity */
public class SettingActivity extends AppCompatActivity {
    Button btnSave;
    int mConnectState;
    int mVideoDisplayPort;
    Spinner spinnerCapute;
    Spinner spinnerColor;
    Spinner spinnerTiming;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0443R.layout.activity_setting);
        this.mVideoDisplayPort = CaptureService.mVideoDisplayPort;
        this.spinnerColor = (Spinner) findViewById(C0443R.C0445id.spinner1);
        this.spinnerTiming = (Spinner) findViewById(C0443R.C0445id.spinner2);
        this.mConnectState = ((ShotApplication) getApplication()).getConnectState();
        SharedPreferences sharedPref = getSharedPreferences("setting", 0);
        int color0 = sharedPref.getInt(getString(C0443R.string.sharedPref_color), -1);
        int timing0 = sharedPref.getInt(getString(C0443R.string.sharedPref_timing), -1);
        if (this.mConnectState == 1) {
            List<String> listColor = new ArrayList<>();
            listColor.add("High quality");
            listColor.add("Low quality");
            this.spinnerColor.setAdapter(new ArrayAdapter<>(this, 17367048, listColor));
            if (this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_HDMI && this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_VGA && this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_DIGITAL) {
                if (this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_YPBPR) {
                    if (this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_CVBS && this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_SVIDEO && this.mVideoDisplayPort != C0442Util.DataType.VIDEO_PORT.VIDEO_OUTPUT_PORT_CVBS_SVIDEO) {
                        List<String> list = new ArrayList<>();
                        list.add(String.valueOf(this.mVideoDisplayPort));
                        this.spinnerTiming.setAdapter(new ArrayAdapter<>(this, 17367049, list));
                        switch (timing0) {
                            case -1:
                            case 1:
                                this.spinnerTiming.setSelection(1);
                                break;
                            case 0:
                                this.spinnerTiming.setSelection(0);
                                break;
                            case 2:
                                this.spinnerTiming.setSelection(2);
                                break;
                            default:
                                this.spinnerTiming.setSelection(1);
                                break;
                        }
                    } else {
                        List<String> list2 = new ArrayList<>();
                        list2.add("720*576");
                        list2.add("720*480");
                        this.spinnerTiming.setAdapter(new ArrayAdapter<>(this, 17367049, list2));
                        switch (timing0) {
                            case -1:
                            case 0:
                                this.spinnerTiming.setSelection(0);
                                break;
                            case 1:
                                this.spinnerTiming.setSelection(1);
                                break;
                        }
                    }
                } else {
                    List<String> list3 = new ArrayList<>();
                    list3.add("1920*1080");
                    list3.add("1280*720");
                    list3.add("720*576");
                    list3.add("720*480");
                    this.spinnerTiming.setAdapter(new ArrayAdapter<>(this, 17367049, list3));
                    switch (timing0) {
                        case -1:
                        case 1:
                            this.spinnerTiming.setSelection(1);
                            break;
                        case 0:
                            this.spinnerTiming.setSelection(0);
                            break;
                        case 2:
                            this.spinnerTiming.setSelection(2);
                            break;
                        case 3:
                            this.spinnerTiming.setSelection(3);
                            break;
                        default:
                            this.spinnerTiming.setSelection(1);
                            break;
                    }
                }
            } else {
                List<String> list4 = new ArrayList<>();
                list4.add("1920*1080");
                list4.add("1280*720");
                list4.add("800*600");
                this.spinnerTiming.setAdapter(new ArrayAdapter<>(this, 17367049, list4));
                switch (timing0) {
                    case -1:
                    case 1:
                        this.spinnerTiming.setSelection(1);
                        break;
                    case 0:
                        this.spinnerTiming.setSelection(0);
                        break;
                    case 2:
                        this.spinnerTiming.setSelection(2);
                        break;
                    default:
                        this.spinnerTiming.setSelection(1);
                        break;
                }
            }
            Spinner spinner = this.spinnerColor;
            if (color0 < 0) {
                color0 = 0;
            }
            spinner.setSelection(color0);
        } else {
            this.spinnerColor.setAdapter(new ArrayAdapter<>(this, 17367049, new ArrayList<>()));
            this.spinnerTiming.setAdapter(new ArrayAdapter<>(this, 17367049, new ArrayList<>()));
        }
        this.btnSave = (Button) findViewById(C0443R.C0445id.btnSave);
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int color = SettingActivity.this.spinnerColor.getSelectedItemPosition();
                int timing = SettingActivity.this.spinnerTiming.getSelectedItemPosition();
                SharedPreferences.Editor editor = SettingActivity.this.getSharedPreferences("setting", 0).edit();
                editor.putInt(SettingActivity.this.getString(C0443R.string.sharedPref_color), color);
                editor.putInt(SettingActivity.this.getString(C0443R.string.sharedPref_timing), timing);
                editor.commit();
                SettingActivity.this.sendBroadcast(new Intent("com.ms.ms2160.setting_change"));
                SettingActivity.this.finish();
            }
        });
        if (this.mConnectState == 1) {
            this.btnSave.setEnabled(true);
        } else {
            this.btnSave.setEnabled(false);
        }
    }

    /* renamed from: com.ms.ms2160.myapplication.SettingActivity$Dict */
    public class Dict implements Serializable {

        /* renamed from: id */
        private Integer f45id;
        private String text;

        public Dict() {
        }

        public Dict(Integer id, String text2) {
            this.f45id = id;
            this.text = text2;
        }

        public Integer getId() {
            return this.f45id;
        }

        public void setId(Integer id) {
            this.f45id = id;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text2) {
            this.text = text2;
        }

        public String toString() {
            return this.text;
        }
    }
}
