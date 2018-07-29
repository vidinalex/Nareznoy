package com.example.vidinalex.helpme.helpers;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

public class PermissionManager {
    public static final String[] DEFAULT_PERMISION_PACK = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE};

    public static void checkPermissionsAndRequest(AppCompatActivity activity, String[] permissions)
    {
        boolean[] checked = new boolean[permissions.length];
        int k=0;

        for (int i = 0; i < permissions.length; i++) {
            if(activity.checkSelfPermission(permissions[i])== PackageManager.PERMISSION_DENIED) {
                checked[i] = false;
                k++;
            }
            else
                checked[i] = true;
        }

        if(k!=0)
        {
            String[] permissionsToRequest = new String[k];
            int j=0;

            for (int i = 0; i < k; i++) {
                if(!checked[i]) {
                    permissionsToRequest[j] = permissions[i];
                    j++;
                }
            }

            //TODO настроить коллбэк на получение или отклонение пермишна
            activity.requestPermissions(permissionsToRequest, 777);
        }


    }
}
