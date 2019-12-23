package com.ethan.imapp.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {


    public static void requestPermission(Context context, String[] permissions) {
        for (String permission: permissions){
            if (ContextCompat.checkSelfPermission(context,
                    permission) != PackageManager.PERMISSION_GRANTED) { //表示未授权时
                //进行授权
                ActivityCompat.requestPermissions((Activity) context,
                        permissions, 1);
            }
        }
    }
}
