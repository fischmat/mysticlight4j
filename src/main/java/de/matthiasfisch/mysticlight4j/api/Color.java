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
}
