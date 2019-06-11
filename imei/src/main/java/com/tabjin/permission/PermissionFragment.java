package com.tabjin.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

public class PermissionFragment extends Fragment {

    public static PermissionFragment newInstance() {

        Bundle args = new Bundle();

        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setReenterTransition(true);
    }

    private CallbackWrapper.PermissionsCallback mPermissionsCallback;

    private CallbackWrapper.EachPermissionCallback mEachPermissionCallback;

    public void requestPermissions(String[] permissions,CallbackWrapper.PermissionsCallback callback){
        mPermissionsCallback = callback;
        requestPermissions(permissions,0);
    }

    public void requestEachPermission(String[] permissions,CallbackWrapper.EachPermissionCallback eachPermissionCallback){
        mEachPermissionCallback = eachPermissionCallback;
        requestPermissions(permissions,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allGranted = false;
        if(requestCode == 0){
            for(int gr:grantResults){
                if(gr != 0){
                    allGranted = false;
                    break;
                }else{
                    allGranted = true;
                }
            }
            if(allGranted){
                mPermissionsCallback.onGranted();
            }else{
                mPermissionsCallback.onDenied();
            }
        }else if(requestCode == 1){
            for(int i=0;i<grantResults.length;i++){
                com.tabjin.permission.Permission permission = null;
                if(grantResults[i] == 0){
                    permission = new com.tabjin.permission.Permission(true,false,permissions[i]);
                }else{
                    if(shouldShowRequestPermissionRationale(permissions[i])){
                        permission = new com.tabjin.permission.Permission(false,true,permissions[i]);
                    }else{
                        permission = new com.tabjin.permission.Permission(false,false,permissions[i]);
                    }
                }
                mEachPermissionCallback.onResult(permission);
            }
        }
    }

    /**
     * 显示没有权限的对话框
     * @param context
     * @param text
     */
    public void showDialog(final Activity context, String text, final CallbackWrapper.ResultCallback callback){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请授予权限！");
        builder.setMessage(text);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startSetting(callback);
            }
        });
        builder.show();
    }

    private CallbackWrapper.ResultCallback resultCallback;

    /**
     * startActivityForResult   统一处理
     * @param intent
     * @param callback
     */
    public  void startActivityForResult(Intent intent,CallbackWrapper.ResultCallback callback){
        resultCallback = callback;
        startActivityForResult(intent,10);
    }

    /**
     * 跳转应用的设置详情页
     */
    public void startSetting(CallbackWrapper.ResultCallback callback){
        if(getActivity() == null){
            return;
        }
        resultCallback = callback;
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, 10);
        }catch (Exception e){
            //防止没有对应的activity
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10&&resultCallback != null){
            resultCallback.onResult(requestCode,resultCode,data);
        }
    }
}
