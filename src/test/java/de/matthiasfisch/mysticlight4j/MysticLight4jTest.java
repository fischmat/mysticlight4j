package de.matthiasfisch.mysticlight4j;

import de.matthiasfisch.mysticlight4j.api.DeviceInfo;
import de.matthiasfisch.mysticlight4j.api.MysticLightAPI;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "de.matthiasfisch.mysticlight4j.api.MysticLightAPI")
public class MysticLight4jTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Path workingDirectory;

    @Before
    public void setUp() throws Exception {
        mockStatic(MysticLightAPI.class);
        workingDirectory = Paths.get(System.getProperty("user.dir"));
    }

    @Test
    public void testCTOR_noArgumentsAndNoElevatedPrivileges_illegalArgumentException() {
        // Arrange
        when(MysticLightAPI.isProcessElevated()).thenReturn(false);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The JVM must run with administrator privileges in order to control Mystic Light devices.");

        // Act + Assert - via rule
        new MysticLight4j();
    }

    @Test
    public void testCTOR_elevatedPrivilegesRequiredAndNoElevatedPrivileges_illegalArgumentException() {
        // Arrange
        when(MysticLightAPI.isProcessElevated()).thenReturn(false);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The JVM must run with administrator privileges in order to control Mystic Light devices.");

        // Act + Assert - via rule
        new MysticLight4j(true, workingDirectory);
    }

    @Test
    public void testCTOR_noElevatedPrivilegesRequiredAndNoElevatedPrivileges_illegalArgumentException() {
        // Arrange
        when(MysticLightAPI.isProcessElevated()).thenReturn(false);

        // Act
        new MysticLight4j(false, workingDirectory);

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.initialize();
    }

    @Test
    public void testCTOR_noArgumentsAndElevatedPrivileges_apiInitialized() throws Exception {
        // Arrange
        when(MysticLightAPI.isProcessElevated()).thenReturn(true);

        // Act
        new MysticLight4j();

        // Assert
        verifyStatic(MysticLightAPI.class);
        MysticLightAPI.initialize();
    }

    @Test
    public void testGetAllAvailableDevices_noDevices_emptyResult() {
        // Arrange
        when(MysticLightAPI.isProcessElevated()).thenReturn(true);
        when(MysticLightAPI.getDeviceInfo()).thenReturn(new DeviceInfo[]{});
        final MysticLight4j subject = new MysticLight4j();

        // Act
        final Collection<Device> result = subject.getAllAvailableDevices();

        // Assert
        assertThat(result, is(not(nullValue())));
        assertThat(result.size(), is(0));
    }
}