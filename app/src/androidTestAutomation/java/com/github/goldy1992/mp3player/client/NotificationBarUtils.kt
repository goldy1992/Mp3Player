package com.github.goldy1992.mp3player.client

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.uiautomator.*


object NotificationBarUtils {

    private const val SYSTEM_UI_PACKAGE : String = "com.android.systemui"
    private const val ANDROID_PLAY_PAUSE_ICON_NOT_EXPANDED : String = "android:id/action0"
    private const val ANDROID_PLAY_PAUSE_ICON_EXPANDED : String = "android:id/action2"
    private const val ANDROID_EXPAND_ICON : String = "android:id/expand_button"
    private const val PLAY_DESCRIPTION : String = "Play"
    private const val PAUSE_DESCRIPTION : String = "Pause"
    private const val NOTIFICATION_STACK_CONTROLLER_ID = "$SYSTEM_UI_PACKAGE:id/notification_stack_scroller"

    fun pauseFromNotificationBar(uiDevice: UiDevice) {
//        val selector : UiSelector = UiSelector()
//                .packageName(SYSTEM_UI_PACKAGE)
//                .description(PAUSE_DESCRIPTION)
//                .resourceId(ANDROID_PLAY_PAUSE_ICON)
        val selector : BySelector = By.pkg(SYSTEM_UI_PACKAGE)
                .descContains(PAUSE_DESCRIPTION)
                .res(ANDROID_PLAY_PAUSE_ICON_NOT_EXPANDED)
        val uiObject : UiObject2 = uiDevice.findObject(selector)
        uiObject.click()
    }

    fun playFromNotificationBar(uiDevice: UiDevice) {
       // uiDevice.openNotification()
        Log.d("clickObj", "opened notifications")

     //   val description = context.getString(R.string.channel_description)
        var selector : UiSelector = UiSelector()
                .packageName(SYSTEM_UI_PACKAGE)
                .className("android.widget.ImageButton")
                .resourceId(ANDROID_PLAY_PAUSE_ICON_EXPANDED)
        Log.d("clickObj", "awaited system ui")
        var uiObject : UiObject = uiDevice.findObject(selector)
        if (!uiObject.exists()) {

            selector  = UiSelector()
            .packageName(SYSTEM_UI_PACKAGE)
                    .className("android.widget.ImageButton")
                    .resourceId(ANDROID_PLAY_PAUSE_ICON_NOT_EXPANDED)
            uiObject = uiDevice.findObject(selector)
        }
        uiObject.click()

        Log.d("clickObj", "clicked play")

    }

    /** [Intent.ACTION_CLOSE_SYSTEM_DIALOGS] is deprecated but as per documentation:
     * https://developer.android.com/about/versions/12/behavior-changes-all it will still work for
     * instrumentation tests. */
    fun closeNotifications(context : Context) {

        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }
}