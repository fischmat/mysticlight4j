package de.matthiasfisch.mysticlight4j.api;

import lombok.*;
import org.apache.commons.lang3.Validate;

@Getter
@EqualsAndHashCode
@ToString
public final class DeviceInfo {
    private final String deviceType;
    private final int ledCount;

    public DeviceInfo(@NonNull final String deviceType, @NonNull final int ledCount) {
        this.deviceType = deviceType;
        this.ledCount = ledCount;
    }
}
