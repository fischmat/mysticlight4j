package de.matthiasfisch.mysticlight4j.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MysticLightAPI {
    static {
        // FIXME
        System.load("C:\\Users\\Matthias Fisch\\Code\\IdeaProjects\\mysticlight4j_native.dll");
    }

    /**
     * Interaction with the Mystic Light API requires the process to run with administrator privileges.
     * This is a helper method for checking if the process is running with elevated privileges and is not actually part
     * of the Mystic Light API.
     * @return Returns true if the JVM is running with elevated privileges.
     */
    public static native boolean isProcessElevated();

    /**
     * Initializes the Mystic Light API. This method must be called before any other method of the Mystic Light API
     * is called.
     * @throws MysticLightAPIException Thrown if the an error occurs while initializing.
     */
    public static native void initialize() throws MysticLightAPIException;

    /**
     * Returns information about all installed devices that are compatible with Mystic Light. The information contains a
     * device ID and the number of LEDs of the device.
     * @return Returns information about all Mystic Light devices installed.
     * @throws MysticLightAPIException Thrown if the API was not initialized or the call timed out.
     */
    public static native DeviceInfo[] getDeviceInfo() throws MysticLightAPIException;

    public static native String[] getDeviceName(String device) throws MysticLightAPIException;

    public static native String getDeviceNameEx(String device, int deviceId) throws MysticLightAPIException;

    public static native LedInfo getLedInfo(String device, int index) throws MysticLightAPIException;

    public static native String[] getLedName(String device) throws MysticLightAPIException;

    public static native Color getLedColor(String device, int ledIndex) throws MysticLightAPIException;

    public static native String getLedStyle(String device, int ledIndex) throws MysticLightAPIException;

    public static native int getLedMaxBright(String device, int ledIndex) throws MysticLightAPIException;

    public static native int getLedBright(String device, int ledIndex) throws MysticLightAPIException;

    public static native int getLedMaxSpeed(String device, int ledIndex) throws MysticLightAPIException;

    public static native int getLedSpeed(String device, int ledIndex) throws MysticLightAPIException;

    public static native void setLedColor(String device, int ledIndex, Color color) throws MysticLightAPIException;

    public static native void setLedColors(String device, int ledIndex, String[] ledNames, Color color) throws MysticLightAPIException;

    public static native void setLedColorEx(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException;

    public static native void setLedColorSync(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException;

    public static native void setLedStyle(String device, int ledIndex, String style) throws MysticLightAPIException;

    public static native void setLedBright(String device, int ledIndex, int level) throws MysticLightAPIException;

    public static native void setLedSpeed(String device, int ledIndex, int level) throws MysticLightAPIException;
}
