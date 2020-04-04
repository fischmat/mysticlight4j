package de.matthiasfisch.mysticlight4j.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MysticLightAPI {
    private static final String NATIVE_DLL_NAME_X86 = "mysticlight4j_native.dll";
    private static final String NATIVE_DLL_NAME_X64 = "mysticlight4j_native_x64.dll";

    // Load the native wrapper DLL on class initialization
    static {
        final String operatingSystem = System.getProperty("os.name").toLowerCase(Locale.getDefault());
        if(!operatingSystem.startsWith("windows")) {
            throw new IllegalStateException("Mystic Light is only supported on Microsoft Windows operating systems");
        }
        final String userDirPath = System.getProperty("user.dir");
        final boolean isX86 = System.getProperty("os.arch").toLowerCase(Locale.getDefault()).equals("x86");
        final String dllVersion = isX86 ? NATIVE_DLL_NAME_X86 : NATIVE_DLL_NAME_X64;
        final File nativeWrapperDll = new File(userDirPath + File.separator + dllVersion);
        if (!nativeWrapperDll.canRead()) {
            throw new IllegalStateException(String.format("Could not find native DLL %s in path %s", NATIVE_DLL_NAME_X86, userDirPath));
        }
        System.load(nativeWrapperDll.getAbsolutePath());
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
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native void initialize() throws MysticLightAPIException;

    /**
     * Returns information about all installed devices that are compatible with Mystic Light. The information contains a
     * device ID and the number of LEDs of the device.
     * @return Returns information about all Mystic Light devices installed.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native DeviceInfo[] getDeviceInfo() throws MysticLightAPIException;

    public static native String[] getDeviceName(String device) throws MysticLightAPIException;

    public static native String getDeviceNameEx(String device, int deviceId) throws MysticLightAPIException;

    /**
     * Returns information about a specific LED of a Mystic Light device. This information contains the name of the LED
     * and the styles supported by the LED.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param index The index of the LED.
     * @return Returns information about the specifed LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native LedInfo getLedInfo(String device, int index) throws MysticLightAPIException;

    /**
     * This function retrieves the all LED name within LED area of specific device.
     * Note: This function apparently may return an empty array for some devices despite there are LEDs accessible.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @return Returns the names of the LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native String[] getLedName(String device) throws MysticLightAPIException;

    /**
     * Returns the current color of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the color of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native Color getLedColor(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Returns the currently active style of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the active style of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native String getLedStyle(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Returns the maximum brightness value of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the maximum brightness value of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native int getLedMaxBright(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Returns the current brightness level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the current brightness level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native int getLedBright(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Returns the maximum speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the maximum speed level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native int getLedMaxSpeed(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Returns the current speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the current speed level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native int getLedSpeed(String device, int ledIndex) throws MysticLightAPIException;

    /**
     * Sets the color of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param color The color to set.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native void setLedColor(String device, int ledIndex, Color color) throws MysticLightAPIException;

    public static native void setLedColors(String device, int ledIndex, String[] ledNames, Color color) throws MysticLightAPIException;

    public static native void setLedColorEx(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException;

    public static native void setLedColorSync(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException;

    /**
     * Sets the active style of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param style The style to set. Must be one of the styles returned by {@link #getLedInfo(String, int)} for this LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native void setLedStyle(String device, int ledIndex, String style) throws MysticLightAPIException;

    /**
     * Sets the brightness value of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param level The brightness level to set. Must be a value between 0 and {@link #getLedMaxBright(String, int)}.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native void setLedBright(String device, int ledIndex, int level) throws MysticLightAPIException;

    /**
     * Sets the speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param level The speed level to set. Must be a value between 0 and {@link #getLedMaxSpeed(String, int)}.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static native void setLedSpeed(String device, int ledIndex, int level) throws MysticLightAPIException;
}
