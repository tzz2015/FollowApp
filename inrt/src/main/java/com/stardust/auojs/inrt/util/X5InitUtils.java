package com.stardust.auojs.inrt.util;

/**
 * @Author : liuyufei
 * @Date : on 2023-07-23 20:16.
 * @Description :
 */

import android.util.Log;

import com.linsh.utilseverywhere.LogUtils;
import com.stardust.auojs.inrt.App;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

public class X5InitUtils {

    public static final String TAG = "X5InitUtils";
    private static boolean mInit = false;

    private X5InitUtils() {

    }

    public static void init() {
        resetSdk();
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //成功时i为100
                if (i != 100) {
                    initFinish();
                }
                Log.d(TAG, "=========load" + i);
                //tbs内核下载完成回调
            }

            @Override
            public void onInstallFinish(int i) {
                mInit = true;
                Log.d(TAG, "=========finish" + i);
                //内核安装完成回调，
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
                Log.d(TAG, "=========progress" + i);
            }
        });
        QbSdk.initX5Environment(App.Companion.getApp(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e(TAG, "X5加载内核完成");
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                //该方法在第一次安装app打开不会回调
                mInit = b;
                Log.e(TAG, "X5加载内核是否成功:" + b);
                if (!mInit && TbsDownloader.needDownload(App.Companion.getApp(), false) && !TbsDownloader.isDownloading()) {
                    LogUtils.v("initFinish");
                    initFinish();
                }
                AppPrefsUtils.INSTANCE.putBoolean(TBS_INIT_KEY, mInit);
            }
        });
    }

    /**
     * 初始化
     */
    private static void resetSdk() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(true);
//        QbSdk.disableAutoCreateX5Webview();
        //强制使用系统内核
        //QbSdk.forceSysWebView();
    }

    //这个key是sp存储的，用来记录上一次是否加载成功
    //因为有的时候需要立刻用到tbs展示文件，但是tbs可能还没加载好
    //因此我们在sp里面做记录，看是否需要等待加载
    //如果上次加载好了，那这次肯定也很快，可以等待
    //如果上次没加载好，那还是提示用户过一会再用吧
    private static final String TBS_INIT_KEY = "tbs_init_key";

    //该方法是用来判断tbs是否加载完成的，供外部调用
    public static boolean initFinish() {
        //如果上次加载成功，那么便认为当前的未加载成功由于还未加载完等
        //不做额外处理
        if (AppPrefsUtils.INSTANCE.getBoolean(TBS_INIT_KEY)) {
            return mInit;
        }
        //上次没加载完，那就根据状态判断是否重置
        if (!mInit && !TbsDownloader.isDownloading()) {
            QbSdk.reset(App.Companion.getApp());//重置化sdk，这样就清除缓存继续下载了
            resetSdk();
            TbsDownloader.startDownload(App.Companion.getApp());//手动开始下载，此时需要先判定网络是否符合要求
        }
        return mInit;
    }
}

