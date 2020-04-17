package de.matthiasfisch.mysticlight4j;

import de.matthiasfisch.mysticlight4j.api.DeviceInfo;
import de.matthiasfisch.mysticlight4j.api.LedInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import lombok.NonNull;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "de.matthiasfisch.mysticlight4j.api.MysticLightAPI")
public class DeviceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        mockStatic(MysticLightAPI.class);
    }

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
        when(MysticLightAPI.getLedInfo(deviceType, 1)).thenReturn(new LedInfo(deviceType, 1, "LED2", new String[] {"style1"}));
        when(MysticLightAPI.getLedInfo(deviceType, 2)).thenReturn(new LedInfo(deviceType, 2, "LED3", new String[] {"style1"}));

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
    public void testGetName_noParameters_nativeFunctionCalled() {
        // Arrange
        final String deviceType = "deviceType";
        final String deviceName = "deviceName";
        final Device subject = new Device(new DeviceInfo(deviceType, 1));
        when(MysticLightAPI.getLedInfo(deviceType, 0)).thenReturn(new LedInfo(deviceType, 0, "LED1", new String[] {"style1"}));
        when(MysticLightAPI.getDeviceNameEx(deviceType, 0)).thenReturn(deviceName);

        // Act
        final String result = subject.getName();

        // Assert
        assertThat(result, equalTo(deviceName));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getDeviceNameEx(eq(deviceType), eq(0));
    }

    @Test
    public void testGetLED_negativeIndex_illegalArgumentException() {
        // Arrange
        final int ledCount = 3;
        final Device subject = new Device(new DeviceInfo("deviceType", ledCount));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("LED index must be in range [0, %s]", ledCount));

        // Act + Assert - via rule
        subject.getLED(-1);
    }

    @Test
    public void testGetLED_indexGreaterThanMax_illegalArgumentException() {
        // Arrange
        final int ledCount = 3;
        final Device subject = new Device(new DeviceInfo("deviceType", ledCount));
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("LED index must be in range [0, %s]", ledCount));

        // Act + Assert - via rule
        subject.getLED(ledCount);
    }

    @Test
    public void testGetLED_indexExists_correctResult() {
        // Arrange
        final int ledCount = 3;
        final String deviceType = "deviceType";
        final Device subject = new Device(new DeviceInfo("deviceType", ledCount));

        when(MysticLightAPI.getLedInfo(deviceType, 0)).thenReturn(new LedInfo(deviceType, 0, "LED1", new String[] {"style1"}));
        when(MysticLightAPI.getLedInfo(deviceType, 1)).thenReturn(new LedInfo(deviceType, 1, "LED2", new String[] {"style1"}));
        when(MysticLightAPI.getLedInfo(deviceType, 2)).thenReturn(new LedInfo(deviceType, 2, "LED3", new String[] {"style1"}));

        // Act
        final LED led = subject.getLED(1);

        // Assert
        assertThat(led, is(not(nullValue())));
        assertThat(led.getIndex(), is(1));

        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedInfo(eq(deviceType), eq(0));
        MysticLightAPI.getLedInfo(eq(deviceType), eq(1));
        MysticLightAPI.getLedInfo(eq(deviceType), eq(2));
    }

    @Test
    public void testGetLED_noSuchLed_resultEmpty() {
        // Arrange
        final String deviceType = "deviceType";
        when(MysticLightAPI.getLedInfo(deviceType, 0)).thenReturn(new LedInfo(deviceType, 0, "LED1", new String[] {"style1"}));
        final Device subject = new Device(new DeviceInfo(deviceType, 1));

        // Act
        final Optional<LED> result = subject.getLED("unknownName");

        // Assert
        assertThat(result.isPresent(), is(false));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedInfo(eq(deviceType), eq(0));
    }

    @Test
    public void testGetLED_existingLED_resultEmpty() {
        // Arrange
        final String deviceType = "deviceType";
        final String ledName = "LED1";
        when(MysticLightAPI.getLedInfo(deviceType, 0)).thenReturn(new LedInfo(deviceType, 0, ledName, new String[] {"style1"}));
        final Device subject = new Device(new DeviceInfo(deviceType, 1));

        // Act
        final Optional<LED> result = subject.getLED(ledName);

        // Assert
        assertThat(result.isPresent(), is(true));
        assertThat(result.map(LED::getName), equalTo(Optional.of(ledName)));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedInfo(eq(deviceType), eq(0));
    }

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange
        final LED redLed = new LED(new Device(new DeviceInfo("dev1", 1)), 0);
        final LED blackLed = new LED(new Device(new DeviceInfo("dev2", 2)), 1);
        // Act + Assert - via verifier
        EqualsVerifier.forClass(Device.class)
                .withPrefabValues(LED.class, redLed, blackLed)
                .withIgnoredFields("leds")
                .verify();
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