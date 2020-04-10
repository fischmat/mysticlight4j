package de.matthiasfisch.mysticlight4j;

import de.matthiasfisch.mysticlight4j.api.Color;
import de.matthiasfisch.mysticlight4j.api.LedInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.List;

/**
 * An LED of a Mystic Light device. This can either be a single physical LED or a group of physical LEDs such as an
 * LED array on a mainboard.
 */
@Getter
@EqualsAndHashCode
@ToString
public class LED {
    private final Device device;
    private final int index;
    @EqualsAndHashCode.Exclude
    private final LedInfo ledInfo;

    protected LED(@NonNull final Device device, final int index) {
        Validate.isTrue(index >= 0, "The LED index must not be negative.");
        Validate.isTrue(index < device.getNumberOfLEDs(), "The LED index must be less than the number of LEDs of the device.");
        this.device = device;
        this.index = index;
        this.ledInfo = MysticLightAPI.getLedInfo(device.getIdentifier(), index);
    }

    /**
     * @return Returns the human readable name of the LED.
     */
    public String getName() {
        return ledInfo.getName();
    }

    /**
     * Returns the styles that are supported by this LED. The returned styles are human readable names for the styles
     * and can be used e.g. {@link #setStyle(String)}.
     * @return Returns the styles available for this LED.
     */
    public List<String> getAvailableStyles() {
        return Arrays.asList(ledInfo.getStyles());
    }

    /**
     * @return Returns the currently active color of the LED.
     */
    public Color getColor() {
        return MysticLightAPI.getLedColor(device.getIdentifier(), index);
    }

    /**
     * @return Returns the currently set style of the LED.
     */
    public String getStyle() {
        return MysticLightAPI.getLedStyle(device.getIdentifier(), index);
    }

    /**
     * Returns the maximum brightness level of the LED. This is the maximum value that can be set with {@link #setBrightnessLevel(int)}.
     * @return The maximum brightness level.
     */
    public int getMaximumBrightnessLevel() {
        return MysticLightAPI.getLedMaxBright(device.getIdentifier(), index);
    }

    /**
     * @return Returns the current brightness level of the LED.
     */
    public int getBrightnessLevel() {
        return MysticLightAPI.getLedBright(device.getIdentifier(), index);
    }

    /**
     * Returns the maximum speed level of the LED. This is the maximum value that can be set with {@link #setSpeedLevel(int)}.
     * @return Returns the maximum speed level.
     */
    public int getMaximumSpeedLevel() {
        return MysticLightAPI.getLedMaxSpeed(device.getIdentifier(), index);
    }

    /**
     * @return Returns the current speed level of the LED.
     */
    public int getSpeedLevel() {
        return MysticLightAPI.getLedSpeed(device.getIdentifier(), index);
    }

    /**
     * Sets the color of the LED.
     * @param color The color to set.
     */
    public void setColor(@NonNull final Color color) {
        MysticLightAPI.setLedColor(device.getIdentifier(), index, color);
    }

    /**
     * Sets the color of the LED.
     * @param red The red color channel in range [0,255].
     * @param green The green color channel in range [0,255].
     * @param blue The blue color channel in range [0,255].
     */
    public void setColor(final int red, final int green, final int blue) {
        setColor(new Color((short) red, (short) green, (short) blue));
    }

    /**
     * Sets the active style of the LED. This must be one of the values returned by {@link #getAvailableStyles()}.
     * @param style The style to set.
     */
    public void setStyle(@NonNull final String style) {
        Validate.isTrue(Arrays.asList(ledInfo.getStyles()).contains(style), "The given style is not available for the LED.");
        MysticLightAPI.setLedStyle(device.getIdentifier(), index, style);
    }

    /**
     * Sets the brightness level of the LED.
     * @param brightnessLevel The brightness level in range [0, {@link #getMaximumBrightnessLevel()}].
     */
    public void setBrightnessLevel(final int brightnessLevel) {
        Validate.inclusiveBetween(0, getMaximumBrightnessLevel(), brightnessLevel, "The brightness level is out of range.");
        MysticLightAPI.setLedBright(device.getIdentifier(), index, brightnessLevel);
    }

    /**
     * Sets the animation speed level of the LED.
     * @param speedLevel The speed level of the LED in range [0, {@link #getMaximumSpeedLevel()}].
     */
    public void setSpeedLevel(final int speedLevel) {
        Validate.inclusiveBetween(0, getMaximumSpeedLevel(), speedLevel, "The speed level is out of range.");
        MysticLightAPI.setLedSpeed(device.getIdentifier(), index, speedLevel);
    }
}
