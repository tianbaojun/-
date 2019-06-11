package com.tabjin.imei;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tabjin.Dialog.TipDialogBuilder;
import com.tabjin.autoSplit.AutoSplitTextView;
import com.tabjin.permission.CallbackWrapper;
import com.tabjin.permission.PermissionUtil;

import java.util.UUID;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity implements CallbackWrapper.PermissionsCallback, View.OnClickListener {

    AutoSplitTextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= findViewById(R.id.tv);
        findViewById(R.id.button).setOnClickListener(this);
        /*if(PermissionUtil.chechPermission(this,READ_PHONE_STATE)){
            tv.setText(getIMEI());
        }else{
            PermissionUtil.requestPermissions(this,new String[]{READ_PHONE_STATE},this);
        }*/
        String content = "just youtsdfsdf like dfefe  sjdfeijfew wefwefwe wefwefwef wefwefwefewf";
        tv.setText(content);
//        tv.measure();



    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        String content = "我的世界我的世界我的世界我的世界我的世界我的世界我的世界just youtsdfsdf like dfefe  sjdfeijfew wefwefwe wefwefwef wefwefwefewf";
        tv.setText(content);
        Log.e("main","content");
        String str = autoSplitText(tv,content);
        Log.e("main",str);
        tv.setText(str);
    }*/

    public String getUUID(){
        return UUID.randomUUID().toString();
    }

    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        return imei;
    }

    @Override
    public void onGranted() {
        tv.setText(getIMEI());
    }

    @Override
    public void onDenied() {
        PermissionUtil.showDialog(this, "请申请读取手机状态权限，否则无法正常使用！",
                new CallbackWrapper.ResultCallback() {
                    @Override
                    public void onResult(int requestCode, int resultCode, Intent intent) {
                        Log.e("main",""+resultCode);
                        PermissionUtil.requestPermissions(MainActivity.this,new String[]{READ_PHONE_STATE},MainActivity.this);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                new TipDialogBuilder(this, R.layout.popup_tip,R.style.appalertdialog)
                        .setOnSureClickListener(R.id.tv_ok, new TipDialogBuilder.OnClickListener() {
                            @Override
                            public void onClick(@NonNull View v, @NonNull AlertDialog dialog1) {
                                dialog1.dismiss();
                            }
                        }).setCancelable(false)
                .show();
                break;
        }
    }



    private String autoSplitText(final TextView tv,String rawText) {
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }
}
