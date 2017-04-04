package com.passer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by ZCYL on 2017/3/19.
 * 用来存放全局的静态数据，如静态字符串等
 */

public class StaticValue {
    //sharedpreference 文件夹的名字
    public static final String SHAREDPREFERENCE_NAME = "conference";
    //保存在shared..里面的工号key值，以及machineid的key
    public static final String SP_WORKER_NO = "worker_no";
    public static final String SP_MACHINE_ID = "machine_id";

    //用来调用后台进行获取身份证图片的key
    public static final String APP_KEY = "bECibCez6_7milQQ_-jhgKmfHkd8WszQ";
    public static final String APP_SECRET = "ZIDwFYPoaf9eR2zXbONPmT0B1HyM1m1o";




    public static StringBuffer UUID_Builder = new StringBuffer();//用来构造UUID

    /**
     * ZC写的。哪个界面需要就由谁来获取
     * @param context 上下文
     * @return SP
     */
    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(SHAREDPREFERENCE_NAME,MODE_PRIVATE);
    }

    //手机系列号
    private static String machinId;
    public static String getMachinId(Context context) {
        if (TextUtils.isEmpty(machinId)) {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            machinId = TelephonyMgr.getDeviceId();
        }
        return machinId;
    }

    public static void setMachineId(String machinId) {
        StaticValue.machinId = machinId;
    }

    /**
     * 获取应用名
     *
     * @param context 上下文
     * @return 应用名
     */
    public static String getApplicationName(Activity context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }


    /**
     * 获取MAC地址
     * 只有在WIFI打开的情况下才可以获取
     * @return
     */
    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return macSerial;
    }

    /**
     * 判断WIFI是否有打开，而不是判断WIFI是否有连接上
     * @param context
     * @return
     */
    public static boolean isWifiOpen(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
           return true;
        } else {
           return false;
        }
    }


}
