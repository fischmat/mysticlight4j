package de.matthiasfisch.mysticlight4j;

import de.matthiasfisch.mysticlight4j.api.Color;
import de.matthiasfisch.mysticlight4j.api.DeviceInfo;
import de.matthiasfisch.mysticlight4j.api.LedInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "de.matthiasfisch.mysticlight4j.api.MysticLightAPI")
public class LEDTest {
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final int DEVICE_LED_COUNT = 1;
    private static final String LED_NAME = "LED_NAME";
    private static final String LED_STYLE1 = "Rainbow";
    private static final String LED_STYLE2 = "Flash";
    private static final String[] LED_STYLES = new String[] { LED_STYLE1, LED_STYLE2 };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LED subject;
    private Device device;

    @Before
    public void setUp() {
        mockStatic(MysticLightAPI.class);
        when(MysticLightAPI.getLedInfo(eq(DEVICE_ID), eq(0))).thenReturn(new LedInfo(DEVICE_ID, 0, LED_NAME, LED_STYLES));
        device = new Device(new DeviceInfo(DEVICE_ID, DEVICE_LED_COUNT));
        subject = device.getLED(0);
    }

    @Test
    public void testCTOR_deviceIsNull_nullPointerException() {
        // Arrange
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("device");

        // Act + Assert
        new LED(null, 2);
    }

    @Test
    public void testCTOR_indexIsNegative_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The LED index must not be negative.");

        // Act + Assert - via rule
        new LED(device, -1);
    }

    @Test
    public void testCTOR_indexIsEqualToLEDCount_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The LED index must be less than the number of LEDs of the device.");

        // Act + Assert - via rule
        new LED(device, DEVICE_LED_COUNT);
    }

    @Test
    public void testCTOR_indexIsGreaterThanLEDCount_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The LED index must be less than the number of LEDs of the device.");

        // Act + Assert - via rule
        new LED(device, DEVICE_LED_COUNT + 1);
    }

    @Test
    public void testCTOR_indexIsValid_correctlyConstructed() {
        // Arrange
        final String name = "LED name";
        final String[] styles = new String[] {"style1"};
        final LedInfo ledInfo = new LedInfo(DEVICE_ID, 0, name, styles);
        when(MysticLightAPI.getLedInfo(eq(DEVICE_ID), eq(0))).thenReturn(ledInfo);

        // Act
        final LED subject = new LED(device, 0);

        // Assert
        assertThat(subject.getDevice(), equalTo(device));
        assertThat(subject.getName(), equalTo(name));
        assertThat(subject.getAvailableStyles(), hasItems(styles));

        verifyStatic(MysticLightAPI.class, times(2)); // Once called in setUp
        MysticLightAPI.getLedInfo(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetColor_noParameters_nativeMethodCalled() {
        // Arrange
        final Color color = Color.of(255, 0, 0);
        when(MysticLightAPI.getLedColor(eq(DEVICE_ID), eq(0))).thenReturn(color);

        // Act
        final Color result = subject.getColor();

        // Assert
        assertThat(result, equalTo(color));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedColor(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetStyle_noParameters_nativeMethodCalled() {
        // Arrange
        final String style = "myStyle";
        when(MysticLightAPI.getLedStyle(eq(DEVICE_ID), eq(0))).thenReturn(style);

        // Act
        final String result = subject.getStyle();

        // Assert
        assertThat(result, equalTo(style));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedStyle(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetMaximumBrightnessLevel_noParameters_nativeMethodCalled() {
        // Arrange
        final int maxLevel = 5;
        when(MysticLightAPI.getLedMaxBright(eq(DEVICE_ID), eq(0))).thenReturn(maxLevel);

        // Act
        final int result = subject.getMaximumBrightnessLevel();

        // Assert
        assertThat(result, equalTo(maxLevel));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedMaxBright(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetBrightnessLevel_noParameters_nativeMethodCalled() {
        // Arrange
        final int level = 5;
        when(MysticLightAPI.getLedBright(eq(DEVICE_ID), eq(0))).thenReturn(level);

        // Act
        final int result = subject.getBrightnessLevel();

        // Assert
        assertThat(result, equalTo(level));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedBright(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetMaximumSpeedLevel_noParameters_nativeMethodCalled() {
        // Arrange
        final int maxSpeed = 10;
        when(MysticLightAPI.getLedMaxSpeed(eq(DEVICE_ID), eq(0))).thenReturn(maxSpeed);

        // Act
        final int result = subject.getMaximumSpeedLevel();

        // Assert
        assertThat(result, equalTo(maxSpeed));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedMaxSpeed(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testGetSpeedLevel_noParameters_nativeMethodCalled() {
        // Arrange
        final int speed = 5;
        when(MysticLightAPI.getLedSpeed(eq(DEVICE_ID), eq(0))).thenReturn(speed);

        // Act
        final int result = subject.getSpeedLevel();

        // Assert
        assertThat(result, equalTo(speed));
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.getLedSpeed(eq(DEVICE_ID), eq(0));
    }

    @Test
    public void testSetColor_colorObject_nativeMethodCalled() {
        // Arrange
        final Color color = Color.of(123, 123, 100);

        // Act
        subject.setColor(color);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedColor(eq(DEVICE_ID), eq(0), eq(color));
    }

    @Test
    public void testSetColor_colorValues_nativeMethodCalled() {
        // Arrange
        final short red = 123;
        final short green = 124;
        final short blue = 125;
        final Color expectedColor = Color.of(red, green, blue);

        // Act
        subject.setColor(red, green, blue);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedColor(eq(DEVICE_ID), eq(0), eq(expectedColor));
    }

    @Test
    public void testSetStyle_styleIsNull_nullPointerException() {
        // Arrange
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("style");

        // Act + Assert - via rule
        subject.setStyle(null);
    }

    @Test
    public void testSetStyle_styleNotSupported_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The given style is not available for the LED.");

        // Act + Assert - via rule
        subject.setStyle("unknownStyle");
    }

    @Test
    public void testSetStyle_validStyle_nativeMethodCalled() {
        // Arrange

        // Act
        subject.setStyle(LED_STYLE2);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedStyle(eq(DEVICE_ID), eq(0), eq(LED_STYLE2));
    }

    @Test
    public void testSetBrightnessLevel_levelNegative_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The brightness level is out of range.");

        // Act + Assert - via rule
        subject.setBrightnessLevel(-1);
    }

    @Test
    public void testSetBrightnessLevel_levelGreaterThanMax_illegalArgumentException() {
        // Arrange
        final int maxLevel = 5;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The brightness level is out of range.");

        when(MysticLightAPI.getLedMaxBright(eq(DEVICE_ID), eq(0))).thenReturn(maxLevel);

        // Act + Assert - via rule
        subject.setBrightnessLevel(maxLevel + 1);
    }

    @Test
    public void testSetBrightnessLevel_levelIsZero_nativeMethodCalled() {
        // Arrange
        when(MysticLightAPI.getLedMaxBright(eq(DEVICE_ID), eq(0))).thenReturn(5);

        // Act
        subject.setBrightnessLevel(0);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedBright(eq(DEVICE_ID), eq(0), eq(0));
    }

    @Test
    public void testSetBrightnessLevel_levelIsMax_nativeMethodCalled() {
        // Arrange
        final int maxLevel = 5;
        when(MysticLightAPI.getLedMaxBright(eq(DEVICE_ID), eq(0))).thenReturn(maxLevel);

        // Act
        subject.setBrightnessLevel(maxLevel);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedBright(eq(DEVICE_ID), eq(0), eq(maxLevel));
    }

    @Test
    public void testSetSpeedLevel_levelNegative_illegalArgumentException() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The speed level is out of range.");

        // Act + Assert - via rule
        subject.setSpeedLevel(-1);
    }

    @Test
    public void testSetSpeedLevel_levelGreaterThanMax_illegalArgumentException() {
        // Arrange
        final int maxLevel = 5;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The speed level is out of range.");

        when(MysticLightAPI.getLedMaxSpeed(eq(DEVICE_ID), eq(0))).thenReturn(maxLevel);

        // Act + Assert - via rule
        subject.setSpeedLevel(maxLevel + 1);
    }

    @Test
    public void testSetSpeedLevel_levelIsZero_nativeMethodCalled() {
        // Arrange
        when(MysticLightAPI.getLedMaxSpeed(eq(DEVICE_ID), eq(0))).thenReturn(5);

        // Act
        subject.setSpeedLevel(0);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedSpeed(eq(DEVICE_ID), eq(0), eq(0));
    }

    @Test
    public void testSetSpeedLevel_levelIsMax_nativeMethodCalled() {
        // Arrange
        final int maxLevel = 5;
        when(MysticLightAPI.getLedMaxSpeed(eq(DEVICE_ID), eq(0))).thenReturn(maxLevel);

        // Act
        subject.setSpeedLevel(maxLevel);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.setLedSpeed(eq(DEVICE_ID), eq(0), eq(maxLevel));
    }

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange + Act + Assert - via verifier
        final Device redDevice = new Device(new DeviceInfo("dev1", 1));
        final Device blackDevice = new Device(new DeviceInfo("dev2", 1));
        EqualsVerifier.forClass(LED.class)
                .withPrefabValues(Device.class, redDevice, blackDevice)
                .withIgnoredFields("ledInfo")
                .verify();
    }
}