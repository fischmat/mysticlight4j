package de.matthiasfisch.mysticlight4j;

import de.matthiasfisch.mysticlight4j.api.Color;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Main class for accessing Mystic Light devices and LEDs in an object oriented manner.
 * The available devices can be retrieved with {@link #getAllAvailableDevices()} and the LEDs of these devices can be
 * accessed via these objects.
 * If you don't want to use the OOP interface, you can directly access the Mystic Light SDK API functions via the
 * {@link MysticLightAPI} JNI interface.
 */
public class MysticLight4j {
    /**
     * Static flag indicating whether the Mystic Light API was already initialized by this JVM process.
     */
    private static boolean apiInitialized = false;

    /**
     * Initializes a new MysticLight4j instance initializing the native API if necessary.
     * This constructor throws {@link IllegalStateException} if the JVM process is not running with administrator privileges
     * as these are required to access the mystic light API.
     * @throws de.matthiasfisch.mysticlight4j.api.MysticLightAPIException Thrown if initialization of the native API fails.
     */
    public MysticLight4j() {
        this(true);
    }

    /**
     * Initializes a new MysticLight4j instance initializing the native API if necessary.
     * @param requireElevatedPrivileges Whether to enforce elevated privileges.
     * @throws de.matthiasfisch.mysticlight4j.api.MysticLightAPIException Thrown if initialization of the native API fails.
     */
    public MysticLight4j(final boolean requireElevatedPrivileges) {
        if (requireElevatedPrivileges && !MysticLightAPI.isProcessElevated()) {
            throw new IllegalStateException("The JVM must run with administrator privileges in order to control Mystic Light devices.");
        }
        if (!apiInitialized) {
            MysticLightAPI.initialize();
            apiInitialized = true;
        }
    }

    /**
     * @return Returns all accessible Mystic Light devices.
     */
    public Collection<Device> getAllAvailableDevices() {
        return Arrays.stream(MysticLightAPI.getDeviceInfo())
                .map(Device::new)
                .collect(Collectors.toList());
    }

    /**
     * Identifies a LED by turning all other LEDs in the system off and making the specified LED blink.
     * This method is intended for testing during developement.
     * @param led The LED to identify.
     */
    public void identifyLED(@NonNull final LED led) {
        // Turn off all other LEDs remembering their initial styles
        final Map<LED, String> originalStylesByLED = getAllAvailableDevices().stream()
                .flatMap(dev -> dev.getLEDs().stream())
                .filter(l -> !l.equals(led))
                .collect(Collectors.toMap(Function.identity(), LED::getStyle));
        originalStylesByLED.keySet()
                .forEach(l -> l.setStyle("Off"));

        final boolean onOffStylesAvailable = led.getAvailableStyles().contains("Off") && led.getAvailableStyles().contains("Steady");
        try {
            // Toggle the styles with 500ms delays for 5s
            for (int i = 0; i < 10; i++) {
                if (onOffStylesAvailable) {
                    led.setStyle("Steady");
                } else {
                    led.setBrightnessLevel(led.getMaximumBrightnessLevel());
                }
                Thread.sleep(500);
                if (onOffStylesAvailable) {
                    led.setStyle("Off");
                } else {
                    led.setBrightnessLevel(1);
                }
                Thread.sleep(500);
            }
        } catch (final InterruptedException ignored) { }

        // Restore original styles
        originalStylesByLED.keySet().forEach(l -> l.setStyle(originalStylesByLED.get(l)));
    }
}
