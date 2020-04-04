package de.matthiasfisch.mysticlight4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MoreCollectors;
import de.matthiasfisch.mysticlight4j.api.DeviceInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import lombok.*;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter(AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class Device {
    private final DeviceInfo deviceInfo;
    @EqualsAndHashCode.Exclude
    private final ImmutableList<LED> leds;

    protected Device(@NonNull final DeviceInfo deviceInfo) {
        Validate.notBlank(deviceInfo.getDeviceType(), "The device identifier must not be blank");
        Validate.isTrue(deviceInfo.getLedCount() >= 0, "The number of LEDs for device %s must not be negative", deviceInfo.getDeviceType());
        this.deviceInfo = deviceInfo;
        this.leds = IntStream.range(0, getNumberOfLEDs())
                .mapToObj(index -> new LED(this, index))
                .collect(ImmutableList.toImmutableList());
    }

    public int getNumberOfLEDs() {
        return deviceInfo.getLedCount();
    }

    public String getIdentifier() {
        return deviceInfo.getDeviceType();
    }

    public String getName() {
        return MysticLightAPI.getDeviceNameEx(getIdentifier(), 0);
    }

    public List<LED> getLEDs() {
        return leds;
    }

    public Optional<LED> getLED(@NonNull final String ledName) {
        return leds.stream()
                .filter(led -> led.getName().equals(ledName))
                .collect(MoreCollectors.toOptional());
    }
}
