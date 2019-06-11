package com.tabjin.permission;

import android.content.Intent;

public interface CallbackWrapper {

    interface PermissionsCallback{
        void onGranted();

        void onDenied();
    }

    interface EachPermissionCallback{
        void onResult(Permission permission);
    }

    interface ResultCallback{
        void onResult(int requestCode,int resultCode,Intent intent);
    }

}
