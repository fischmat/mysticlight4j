package de.matthiasfisch.mysticlight4j.api;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import de.matthiasfisch.mysticlight4j.WindowsOnlyTest;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Vector;

import static org.junit.Assert.assertTrue;

public class MysticLightAPITest extends WindowsOnlyTest {
    @Test
    public void testInitialization_windowsOperatingSystem_nativeDllLoaded() throws Exception {
        // Arrange
        final Field clLibrariesField = ClassLoader.class.getDeclaredField("loadedLibraryNames");
        clLibrariesField.setAccessible(true);
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        final String osArch = System.getProperty("os.arch").toLowerCase(Locale.getDefault());
        final String expectedLoadedLibrary = osArch.contains("x86") ? MysticLightAPI.NATIVE_DLL_NAME_X86 : MysticLightAPI.NATIVE_DLL_NAME_X64;

        // Act + Assert - check loaded libraries
        final Iterable<String> libraries = (Iterable<String>) clLibrariesField.get(classLoader);

        assertTrue(Streams.stream(libraries).anyMatch(lib -> lib.endsWith(MysticLightAPI.NATIVE_DLL_NAME_X64)));
    }
}