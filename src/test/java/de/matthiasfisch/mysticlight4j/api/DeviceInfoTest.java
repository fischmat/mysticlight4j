package de.matthiasfisch.mysticlight4j.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DeviceInfoTest {
    private static final String DEVICE_TYPE = "MSI_MB";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_deviceTypeNull_nullPointerException() {
        // Arrange
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("deviceType");

        // Act + Assert - via rule
        new DeviceInfo(null, 2);
    }

    @Test
    public void testCTOR_ledCountNegative_correctlyConstructed() {
        // Arrange

        // Act
        final DeviceInfo subject = new DeviceInfo(DEVICE_TYPE, -1);

        // Assert
        assertThat(subject.getDeviceType(), equalTo(DEVICE_TYPE));
        // Native API data wrappers should not validate values
        assertThat(subject.getLedCount(), is(-1));
    }

    @Test
    public void testCTOR_deviceTypeBlank_correctlyConstructed() {
        // Arrange

        // Act
        final DeviceInfo subject = new DeviceInfo("", 1);

        // Assert
        // Native API data wrappers should not validate values
        assertThat(subject.getDeviceType(), equalTo(""));
        assertThat(subject.getLedCount(), is(1));
    }

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange + Act + Assert - via verifier
        EqualsVerifier.forClass(DeviceInfo.class).withIgnoredFields("ledCount").verify();
    }
}