SET TESTS_FILE=tests.zip

@rem adb devices
adb push busybox-android /data/local/busybox-android
adb push %TESTS_FILE% /data/local
adb shell chmod 0577 /data/local/busybox-android #Make it executable
adb shell mkdir /sdcard/.tests
adb shell /data/local/busybox-android unzip /data/local/%TESTS_FILE% /data/local/.tests