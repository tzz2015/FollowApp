package com.stardust.autojs.core.database;

import androidx.annotation.Keep;

@Keep
public class Databases {

    public static Database openDatabase(String name, int version, String desc, long size){
        return new Database();
    }



}
