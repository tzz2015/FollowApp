package com.stardust.autojs.core.inputevent;

import android.util.Log;
import android.view.InputDevice;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
@Keep
public class InputDevices {

    private static final String LOG_TAG = "InputDevices";

    @Nullable
    public static String getTouchDeviceName() {
        InputDevice device = getTouchDevice();
        return  device == null ? null : device.getName();
    }

    @Nullable
    public static InputDevice getTouchDevice() {
        for (int id : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(id);
            Log.d(LOG_TAG, "device: " + device);
            if (supportSource(device, InputDevice.SOURCE_TOUCHSCREEN) || supportSource(device, InputDevice.SOURCE_TOUCHPAD)) {
                return device;
            }
        }
        return null;
    }

    private static boolean supportSource(InputDevice device, int source) {
        return (device.getSources() & source) == source;
    }


}
