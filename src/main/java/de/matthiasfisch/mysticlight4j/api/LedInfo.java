package de.matthiasfisch.mysticlight4j.api;

import lombok.*;
import org.apache.commons.lang3.Validate;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class LedInfo {
    private final String deviceType;
    private final int index;
    private final String name;
    private final String[] styles;
}
