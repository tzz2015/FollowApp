package com.stardust.autojs.core.database;

import android.database.SQLException;

import androidx.annotation.Keep;

@Keep
public interface StatementErrorCallback {

    boolean handleEvent(Transaction transaction, SQLException error);

}
