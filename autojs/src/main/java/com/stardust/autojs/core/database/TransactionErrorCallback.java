package com.stardust.autojs.core.database;

import android.database.SQLException;

import androidx.annotation.Keep;


@Keep
public interface TransactionErrorCallback {

    void handleEvent(SQLException e);
}
