package com.stardust.autojs.core.database;

import androidx.annotation.Keep;

@Keep
public interface TransactionCallback {
    void handleEvent(Transaction transaction);
}
