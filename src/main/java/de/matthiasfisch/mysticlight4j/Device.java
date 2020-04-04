package de.matthiasfisch.mysticlight4j;

import lombok.Value;

@Value
public class Device {
    private final String deviceType;
    private final int ledCount;
}
