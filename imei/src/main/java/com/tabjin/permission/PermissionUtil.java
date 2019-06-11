package com.tabjin.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PermissionUtil {

    private static final String FRAGMENTTAG = "FRAGMENTTAG";

    private PermissionUtil(){}

    private static PermissionFragment getFragemnt(@NonNull FragmentActivity activity){
        FragmentManager manager = activity.getSupportFragmentManager();
        PermissionFragment fragment = (PermissionFragment) manager.findFragmentByTag(FRAGMENTTAG);
        if(fragment == null){
            fragment = PermissionFragment.newInstance();
            manager.beginTransaction()
                    .add(fragment,FRAGMENTTAG)
                    .commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        return fragment;
    }

    /**
     * 所有权限都同意才成功
     * @param activity
     * @param permissions
     * @param callback
     */
    public static void requestPermissions(FragmentActivity activity, String[] permissions,@NonNull CallbackWrapper.PermissionsCallback callback){
        getFragemnt(activity).requestPermissions(permissions,callback);
    }

    /**
     * 逐个权限返回结果
     * @param activity
     * @param permissions
     * @param callback
     */
    public static void requestPermissions(FragmentActivity activity, String[] permissions,@NonNull CallbackWrapper.EachPermissionCallback callback){
        getFragemnt(activity).requestEachPermission(permissions,callback);
    }

    public static void showDialog(FragmentActivity activity, String text, final CallbackWrapper.ResultCallback callback){
        getFragemnt(activity).showDialog(activity,text,callback);
    }

    public static boolean chechPermission(Activity context,String permission){
        return ActivityCompat.checkSelfPermission(context,permission)==PackageManager.PERMISSION_GRANTED;
    }

}
