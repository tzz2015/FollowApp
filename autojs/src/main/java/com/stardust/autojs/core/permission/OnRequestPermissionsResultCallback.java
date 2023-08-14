package com.stardust.autojs.core.permission;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
@Keep
public interface OnRequestPermissionsResultCallback {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
