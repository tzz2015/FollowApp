package com.stardust.autojs.core.database;

import androidx.annotation.Keep;
@Keep
public interface StatementCallback {

    void handleEvent(Transaction transaction, DatabaseResultSet resultSet);
}
