@echo off
title Smart Parking Management System
color 0F
cd /d "%~dp0"

echo =====================================================================
echo            SMART PARKING MANAGEMENT SYSTEM - LAUNCHER
echo =====================================================================
echo.
echo Checking Java installation...
javac -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java Development Kit - JDK was not found in your PATH!
    echo Please make sure JDK is installed and configured in your environment variables.
    echo.
    pause
    exit /b
)

echo Compiling Java source files...
javac parking\*.java
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Please check for any code errors above.
    echo.
    pause
    exit /b
)

echo Compilation successful! Launching application...
echo.
cls

java parking.Main

echo.
echo =====================================================================
echo Application closed. Press any key to exit this window...
echo =====================================================================
pause >nul
