
@ECHO OFF
PowerShell.exe -executionpolicy remotesigned -NoProfile -Command "& {Start-Process PowerShell.exe -ArgumentList '-NoProfile -ExecutionPolicy Bypass -File "./runUiTests.ps1"' -NoExit -Verb RunAs}"

