package com.stardust.auojs.inrt.util;

import android.app.Activity;
import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.jiagu.sdk.OSETSDKProtected;
import com.kc.openset.OSETBanner;
import com.kc.openset.OSETFullVideo;
import com.kc.openset.OSETSDK;
import com.kc.openset.ad.OSETInformationCache;
import com.kc.openset.ad.OSETInsertCache;
import com.kc.openset.ad.OSETInsertHorizontal;
import com.kc.openset.ad.OSETRewardVideoCache;
import com.kc.openset.listener.OSETInitListener;
import com.stardust.app.GlobalAppContext;

public class AdUtils {
    public static boolean isInitAd = false;
    public static final String AppKey = "E6097975B89E83D6";
    public static final String POS_ID_Splash = "7D5239D8D88EBF9B6D317912EDAC6439";
    public static final String POS_ID_Banner = "107EB50EDFE65EA3306C8318FD57D0B3";
    public static final String POS_ID_Insert = "1D273967F51868AF2C4E080D496D06D0";
    public static final String POS_ID_Insert_Horizontal = "592AFB97E4FD2C63EC23AA781FF6E8B0";
    public static final String POS_ID_RewardVideo = "09A177D681D6FB81241C3DCE963DCB46";
    public static final String POS_ID_FullVideo = "D879C3DED01D5CE319CD2751474BA8E4";
    public static final String POS_ID_INFORMATION = "89FEEA66F9228ED3F6420294B89A902B";//原生广告v
    public static final String POS_ID_DRAEINFORMATION = "6328AB893D5DBA6B9D2791B54E1D2C16";//原生Draw广告
    public static final String POS_ID_VIDEOCONTENT = "2A96205DFDDB8D27C784FF31F0625BA4";//视频内容
    public static final String POS_ID_NEWSCONTENT = "4EC4251D616C69030A161A930A938596";//信息流内容
    public static final String POS_ID_NOVELCONTENT = "6EBF6503C9379A85DC95C0AE8D787C35";//小说内容
    public static final String POS_ID_NEWSYDCONTENT = "EBE266AAE65F52C37A28BF2D586132EB";//YD资讯内容
    public static final String POS_ID_SUSPEND = "C20D0FDCA88E06E6718A33279AAD2B4D";//悬浮框
    public static final String POS_ID_NOVEL_TASK = "C4BC47AE2DEE0D663BB14903F1400731";//30s小说任务

    public static String userId = "39AF0C2A82CB8178";


    public static void initAd() {
        if (isInitAd) {
            return;
        }

        OSETSDKProtected.install((Application) GlobalAppContext.get());
        OSETSDK.getInstance().setUserId(userId);
        OSETSDK.getInstance().init((Application) GlobalAppContext.get(), AppKey, new OSETInitListener() {
            @Override
            public void onError(String s) {
                isInitAd = false;
                LogUtils.e("初始化广告失败");
            }

            @Override
            public void onSuccess() {
                isInitAd = true;
                LogUtils.e("初始化广告成功");

            }
        });
    }

    public static void initCache(Activity activity) {
        //在首页开启缓存（首页生命周期比较长）
        OSETRewardVideoCache.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_RewardVideo)
                .startLoad();

        //同激励广告
        OSETInsertCache.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_Insert)
                .startLoad();

        //同激励广告
        OSETInsertHorizontal.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_Insert_Horizontal)
                .startLoad();

        OSETInformationCache.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_INFORMATION)
                .startLoad();

        OSETFullVideo.getInstance()
                .setContext(activity)
                .setPosId(POS_ID_FullVideo)
                .startLoad();

        OSETBanner.getInstance()
                .setContext(activity)
                .setUserId(userId)
                .setPosId(POS_ID_Banner)
                .startLoad();


    }

    public static void destroyAd() {
        OSETRewardVideoCache.getInstance().destroy();
        OSETInsertCache.getInstance().destroy();
        OSETInformationCache.getInstance().destroy();
        OSETInsertHorizontal.getInstance().destroy();
        OSETFullVideo.getInstance().destroy();
        OSETBanner.getInstance().destroy();
    }

}
