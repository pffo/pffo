package com.ffo.ipiker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ffo.ipiker.util.NetWorkUtil;

/**
 * Author: huchunhua
 * Time: 2017/8/30 9:45
 * Package: com.ffo.ipiker.receiver
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 监测网络变化
 */

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //联网
            NetWorkUtil.setNetWork(true);
        } else {
            //断网
            NetWorkUtil.setNetWork(false);
            Toast.makeText(context, "请检查网络连接！", Toast.LENGTH_SHORT).show();
        }
    }


}
