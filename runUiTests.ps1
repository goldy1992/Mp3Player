Remove-Item -path $pwd\test-data -recurse
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory("$pwd\tests.zip", "$pwd\test-data")
adb -e root
adb -e rm -rf /sdcard/test-data
adb -e shell mkdir /sdcard/test-data
adb -e push $pwd\test-data /sdcard/
./gradlew.bat clean build connectedAndroidTest
exit 0
