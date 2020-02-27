package com.github.goldy1992.mp3player.client

import androidx.test.uiautomator.*

object NotificationBarUtils {

    private const val SYSTEM_UI_PACKAGE : String = "com.android.systemui"
    private const val ANDROID_PLAY_PAUSE_ICON : String = "android:id/action0"
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
                .res(ANDROID_PLAY_PAUSE_ICON)
        val uiObject : UiObject2 = uiDevice.findObject(selector)
        uiObject.click()
    }

    fun playFromNotificationBar(uiDevice: UiDevice) {
//        val selector : UiSelector = UiSelector()
//                .packageName(SYSTEM_UI_PACKAGE)
//                .description(PLAY_DESCRIPTION)
//                .resourceId(ANDROID_PLAY_PAUSE_ICON)
        val selector : BySelector = By.pkg(SYSTEM_UI_PACKAGE)
                .descContains(PLAY_DESCRIPTION)
                .res(ANDROID_PLAY_PAUSE_ICON)

        val uiObject : UiObject2 = uiDevice.findObject(selector)
        uiObject.click()
    }

    fun closeNotifications() {
        val notificationStackScroller = UiSelector()
                .packageName(SYSTEM_UI_PACKAGE)
                .resourceId(NOTIFICATION_STACK_CONTROLLER_ID)
        val uiScrollable = UiScrollable(notificationStackScroller)
        uiScrollable.flingToEnd(1)
    }
}