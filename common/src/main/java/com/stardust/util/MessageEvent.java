package com.stardust.util;

import androidx.annotation.Keep;

/**
 * Created by Stardust on 2017/3/12.
 */
@Keep

public class MessageEvent {

    public String message;
    public Object param;

    public MessageEvent(String message, Object param) {
        this.message = message;
        this.param = param;
    }


    public MessageEvent(String message) {
        this.message = message;
    }

}
