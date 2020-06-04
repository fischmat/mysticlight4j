package de.matthiasfisch.mysticlight4j.api;

import lombok.*;
import org.apache.commons.lang3.Validate;

/**
 * Information about the LED of a Mystic Light device.
 * This information includes the identifier of the device the LED belongs to, the index of the LED, the name of the LED
 * and the styles that are available for this LED.
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class LedInfo {
    private final String deviceType;
    private final int index;
    private final String name;
    private final String[] styles;
}
