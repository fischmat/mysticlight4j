package de.matthiasfisch.mysticlight4j.api;

import lombok.*;
import org.apache.commons.lang3.Validate;

/**
 * POJO for 24-bit RGB colors.
 */
@Getter
@EqualsAndHashCode
@ToString
public final class Color {
    private final short red;
    private final short green;
    private final short blue;

    public Color(final short red, final short green, final short blue) {
        Validate.inclusiveBetween(0, 255, red, "Red value must be in range [0, 255].");
        Validate.inclusiveBetween(0, 255, green, "Green value must be in range [0, 255].");
        Validate.inclusiveBetween(0, 255, blue, "Blue value must be in range [0, 255].");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * Creates a color object with the given color values.
     * @param red The red channel value. Must be in range [0, 255].
     * @param green The green channel value. Must be in range [0, 255].
     * @param blue The blue channel value. Must be in range [0, 255].
     * @return Returns the color object with the specified color values.
     */
    public static Color of(final int red, final int green, final int blue) {
        return new Color((short) red, (short) green, (short) blue);
    }
}
