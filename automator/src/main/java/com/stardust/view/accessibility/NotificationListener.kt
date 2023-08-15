package com.stardust.view.accessibility

import androidx.annotation.Keep
import com.stardust.notification.Notification

/**
 * Created by Stardust on 2017/8/1.
 */
@Keep
interface NotificationListener {


    fun onNotification(notification: Notification)


}
