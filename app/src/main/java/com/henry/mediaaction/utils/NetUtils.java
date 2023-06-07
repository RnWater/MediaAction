package com.henry.mediaaction.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.henry.mediaaction.base.BaseApplication;

public class NetUtils {
    public static int  getNetworkState(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //wifi网络
        boolean isWifiConn = networkInfo.isConnected();
        if (isWifiConn) {
            return 2;
        }
        networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //移动网络
        boolean isMobileConn = networkInfo.isConnected();
        if (isMobileConn) {
            return 1;
        }
        if (!isMobileConn && !isWifiConn) {
            return 3;
        }
        return 4;
    }
    //判断是否有网络都链接上了
    public static boolean isNetAvailable(){
        ConnectivityManager manager = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo == null) {
            return false;
        }
        //wifi网络
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo == null) {
            return isWifiConn;
        }
        //移动网络
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn || isWifiConn;
    }
}
