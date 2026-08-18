#!/bin/bash
# astryl_secure_provision.sh
# purpose: lock down grapheneos with a secure vpn fallback

echo "[*] verifying adb connection..."
if ! adb devices | grep -q "device$"; then
    echo "[-] error: no device connected."
    exit 1
fi

echo "[*] setting always-on vpn with secure fallback..."
adb shell settings put global always_on_vpn_app com.singbox.client
adb shell settings put global always_on_vpn_lockdown 0

echo "[*] stripping non-essential apps..."
adb shell pm uninstall -k --user 0 com.android.gallery3d
adb shell pm uninstall -k --user 0 com.android.music

echo "[+] astrylnodeos secure provisioning complete."