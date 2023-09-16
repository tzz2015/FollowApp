package com.stardust.auojs.inrt.data;

import com.mind.data.data.model.SpreadModel;

import org.autojs.autoxjs.inrt.R;

import java.util.List;

/**
 * Created by Stardust on 2017/7/7.
 */

public final class Constants {

    public static String DOUYIN_JS = "douyin.js";
    public static String MAIN_JS = "main.js";

    public static String MAIN1_JS = "main1.js";

    public static String SHARE_JS = "share.js";


    public static String LAST_HOST = "www.link-nemo.com/";
    public static String HOST_URL = "https://" + LAST_HOST;
    public static String ARTICLE_URL = "https://www.link-nemo.com/u/";
    public static String INJECTION_JS = "javascript:(function(){\n" +
            "               var classNames = [\"blog-article-content\",\"bplist\",\"post\"];\n" +
            "               var array=new Array();\n" +
            "               var index=0;\n" +
            "               for(var clsIdx=0;clsIdx < classNames.length; clsIdx ++){\n" +
            "                   var clsName = classNames[clsIdx];\n" +
            "                   var boxs = document.getElementsByClassName(clsName);\n" +
            "                   if(!!boxs){\n" +
            "                       for(var boxIdx=0;boxIdx < boxs.length; boxIdx ++){\n" +
            "                           var box = boxs[boxIdx];\n" +
            "                           var objs = box.getElementsByTagName(\"img\");\n" +
            "                           console.log(objs);\n" +
            "                           if(!!objs){\n" +
            "                              for(var i=0;i<objs.length;i++)  {\n" +
            "                                    objs[i].onclick=function(){\n" +
            "                                       if(array.length == 0){\n" +
            "                                            for(var j=0;j<objs.length;j++)  {\n" +
            "                                                 if(objs[j].src.search(\"common_lodding.gif\") == -1){\n" +
            "                                                     array[index]=objs[j].src;\n" +
            "                                                     index++;\n" +
            "                                                 }\n" +
            "                                            }\n" +
            "                                       }\n" +
            "                                       if(array.indexOf(this.src) == -1){\n" +
            "                                         array[index]=this.src;\n" +
            "                                         index++;\n" +
            "                                       }\n" +
            "                                      window.imagelistener.openImage(this.src,array);\n" +
            "                                    }\n" +
            "                              }\n" +
            "                           }\n" +
            "                       }\n" +
            "                   }\n" +
            "               }})()";


    public static int[] IMAGE_ARRAY = new int[]{
            R.mipmap.icon_header_1_1,
            R.mipmap.icon_header_1_2,
            R.mipmap.icon_header_1_3,
            R.mipmap.icon_header_1_4,
            R.mipmap.icon_header_1_5,
            R.mipmap.icon_header_1_6,
            R.mipmap.icon_header_1_7,
            R.mipmap.icon_header_1_8,
            R.mipmap.icon_header_1_9,
            R.mipmap.icon_header_1_10,
            R.mipmap.icon_header_1_11,
            R.mipmap.icon_header_1_12,
            R.mipmap.icon_header_1_13,
            R.mipmap.icon_header_1_14,
            R.mipmap.icon_header_1_15,
            R.mipmap.icon_header_1_16,
            R.mipmap.icon_header_1_17};
    public static String UPDATE_FUNCTION = "update_function";

    public static List<SpreadModel> SPREAD_URL;

    public static String AD_VIDEO = "视频广告";


}
