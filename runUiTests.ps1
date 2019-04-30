Remove-Item -path $pwd\test-data -recurse
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory("$pwd\tests.zip", "$pwd\test-data")
adb -s emulator-5554 root
adb -s emulator-5554 shell rm -rf /sdcard/test-data
adb -s emulator-5554 shell mkdir /sdcard/test-data
adb -s emulator-5554 push $pwd\test-data /sdcard/
./gradlew.bat clean build connectedAutomationDebugAndroidTest
exit 0
