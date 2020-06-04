package de.matthiasfisch.mysticlight4j.api;

import org.apache.commons.lang3.NotImplementedException;

public class MysticLightAPI {

    /**
     * Interaction with the Mystic Light API requires the process to run with administrator privileges.
     * This is a helper method for checking if the process is running with elevated privileges and is not actually part
     * of the Mystic Light API.
     * @return Returns true if the JVM is running with elevated privileges.
     */
    public static boolean isProcessElevated() {
        return MysticLightNativeBinding.isProcessElevated();
    }

    /**
     * Initializes the Mystic Light API. This method must be called before any other method of the Mystic Light API
     * is called.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static void initialize() throws MysticLightAPIException {
        MysticLightNativeBinding.initialize();
    }

    /**
     * Returns information about all installed devices that are compatible with Mystic Light. The information contains a
     * device ID and the number of LEDs of the device.
     * @return Returns information about all Mystic Light devices installed.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static DeviceInfo[] getDeviceInfo() throws MysticLightAPIException {
        return MysticLightNativeBinding.getDeviceInfo();
    }

    public static String[] getDeviceName(String device) throws MysticLightAPIException {
        return MysticLightNativeBinding.getDeviceName(device);
    }

    public static String getDeviceNameEx(String device, int deviceId) throws MysticLightAPIException {
        return MysticLightNativeBinding.getDeviceNameEx(device, deviceId);
    }

    /**
     * Returns information about a specific LED of a Mystic Light device. This information contains the name of the LED
     * and the styles supported by the LED.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param index The index of the LED.
     * @return Returns information about the specifed LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static LedInfo getLedInfo(String device, int index) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedInfo(device, index);
    }

    /**
     * This function retrieves the all LED name within LED area of specific device.
     * Note: This function apparently may return an empty array for some devices despite there are LEDs accessible.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @return Returns the names of the LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static String[] getLedName(String device) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedName(device);
    }

    /**
     * Returns the current color of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the color of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static Color getLedColor(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedColor(device, ledIndex);
    }

    /**
     * Returns the currently active style of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the active style of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static String getLedStyle(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedStyle(device, ledIndex);
    }

    /**
     * Returns the maximum brightness value of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the maximum brightness value of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static int getLedMaxBright(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedMaxBright(device, ledIndex);
    }

    /**
     * Returns the current brightness level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the current brightness level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static int getLedBright(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedBright(device, ledIndex);
    }

    /**
     * Returns the maximum speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the maximum speed level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static int getLedMaxSpeed(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedMaxSpeed(device, ledIndex);
    }

    /**
     * Returns the current speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @return Returns the current speed level of the specified LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static int getLedSpeed(String device, int ledIndex) throws MysticLightAPIException {
        return MysticLightNativeBinding.getLedSpeed(device, ledIndex);
    }

    /**
     * Sets the color of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param color The color to set.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static void setLedColor(String device, int ledIndex, Color color) throws MysticLightAPIException {
        MysticLightNativeBinding.setLedColor(device, ledIndex, color);
    }

    public static void setLedColors(String device, int ledIndex, String[] ledNames, Color color) throws MysticLightAPIException {
        throw new NotImplementedException("This function is currently not implemented");
    }

    public static void setLedColorEx(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException {
        throw new NotImplementedException("This function is currently not implemented");
    }

    public static void setLedColorSync(String device, int ledIndex, String ledName, Color color, boolean sync) throws MysticLightAPIException {
        throw new NotImplementedException("This function is currently not implemented");
    }

    /**
     * Sets the active style of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param style The style to set. Must be one of the styles returned by {@link #getLedInfo(String, int)} for this LED.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static void setLedStyle(String device, int ledIndex, String style) throws MysticLightAPIException {
        MysticLightNativeBinding.setLedStyle(device, ledIndex, style);
    }

    /**
     * Sets the brightness value of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param level The brightness level to set. Must be a value between 0 and {@link #getLedMaxBright(String, int)}.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static void setLedBright(String device, int ledIndex, int level) throws MysticLightAPIException {
        MysticLightNativeBinding.setLedBright(device, ledIndex, level);
    }

    /**
     * Sets the speed level of a LED of a specific Mystic Light device.
     * @param device The identifier of the device the LED belongs to. This is a value as returned by {@link #getDeviceInfo()}.
     * @param ledIndex The index of the LED.
     * @param level The speed level to set. Must be a value between 0 and {@link #getLedMaxSpeed(String, int)}.
     * @throws MysticLightAPIException Thrown if the native function does not return {@code MLAPI_OK}.
     */
    public static void setLedSpeed(String device, int ledIndex, int level) throws MysticLightAPIException {
        MysticLightNativeBinding.setLedSpeed(device, ledIndex, level);
    }
}
