package de.matthiasfisch.mysticlight4j;

import com.google.common.collect.Lists;
import de.matthiasfisch.mysticlight4j.api.DeviceInfo;
import de.matthiasfisch.mysticlight4j.api.LedInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.verification.VerificationMode;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "de.matthiasfisch.mysticlight4j.api.MysticLightAPI")
public class DeviceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_nullDeviceInfo_nullPointerException() {
        // Arrange
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("deviceInfo");

        // Act + Assert - via rule
        new Device(null);
    }

    @Test
    public void testCTOR_deviceTypeBlank_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The device identifier must not be blank");

        final DeviceInfo deviceInfo = new DeviceInfo("", 2);

        // Act + Assert - via rule
        new Device(deviceInfo);
    }

    @Test
    public void testCTOR_ledCountNegative_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The number of LEDs for device MSI_MB must not be negative");

        final DeviceInfo deviceInfo = new DeviceInfo("MSI_MB", -1);

        // Act + Assert - via rule
        new Device(deviceInfo);
    }

    @Test
    public void testCTOR_validParameter_correctlyConstructed() {
        // Arrange
        final String deviceType = "deviceType";
        final int ledCount = 3;
        final DeviceInfo deviceInfo = new DeviceInfo(deviceType, ledCount);

        when(MysticLightAPI.getLedInfo(deviceType, 0)).thenReturn(new LedInfo(deviceType, 0, "LED1", new String[] {"style1"}));
        when(MysticLightAPI.getLedInfo(deviceType, 1)).thenReturn(new LedInfo(deviceType, 1, "LED1", new String[] {"style1"}));
        when(MysticLightAPI.getLedInfo(deviceType, 2)).thenReturn(new LedInfo(deviceType, 2, "LED1", new String[] {"style1"}));

        // Act
        final Device subject = new Device(deviceInfo);

        // Assert
        assertThat(subject.getDeviceInfo(), equalTo(deviceInfo));
        assertThat(subject.getIdentifier(), equalTo(deviceType));
        assertThat(subject.getNumberOfLEDs(), is(ledCount));
        final List<LED> leds = subject.getLEDs();
        assertThat(leds.size(), is(ledCount));
        assertThat(leds, CoreMatchers.hasItem(ledMatcher(subject, 0)));
        assertThat(leds, CoreMatchers.hasItem(ledMatcher(subject, 1)));
        assertThat(leds, CoreMatchers.hasItem(ledMatcher(subject, 2)));

        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedInfo(eq(deviceType), eq(0));
    }

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange + Act + Assert - via verifier
        EqualsVerifier.forClass(Device.class).withIgnoredFields("leds").verify();
    }

    private Matcher<LED> ledMatcher(@NonNull final Device device, final int index) {
        return new BaseMatcher<LED>() {
            @Override
            public boolean matches(final Object item) {
                if (!(item instanceof LED)) {
                    return false;
                }
                final LED led = (LED) item;
                return led.getDevice().equals(device) && led.getIndex() == index;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText(String.format("LED with device %s and index %s", device.getIdentifier(), index));
            }
        };
    }
}