package de.matthiasfisch.mysticlight4j.api;

import com.google.common.collect.Streams;
import de.matthiasfisch.mysticlight4j.WindowsOnlyTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class MysticLightAPITest extends WindowsOnlyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        // When multiple tests run the is initialize flag may be still set from other tests - set it false
        MysticLightAPI.setInitializationStatus(false);
    }

    @Test
    public void testInitialize_noArguments_nativeDllLoaded() throws Exception {
        // Arrange

        // Act
        MysticLightAPI.initialize();

        // Assert
        assertNativeLibraryLoaded();
    }

    @Test
    public void testInitialize_calledMultipleTimes_nativeDllLoaded() throws Exception {
        // Arrange

        // Act
        MysticLightAPI.initialize();
        MysticLightAPI.initialize();

        // Assert
        assertNativeLibraryLoaded();
    }

    @Test
    public void testInitialize_directorySpecified_nativeDllLoaded() throws Exception {
        // Arrange
        final Path workingDirectory = Paths.get(System.getProperty("user.dir"));

        // Act
        MysticLightAPI.initialize(workingDirectory);

        // Assert
        assertNativeLibraryLoaded();
    }

    @Test
    public void testInitialize_nonExistentPathSpecified_nativeDllLoaded() throws Exception {
        // Arrange
        final Path nonExistentPath = Paths.get(String.format("C:\\%s", UUID.randomUUID()));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("The path %s does not exist.", nonExistentPath.toAbsolutePath().toString()));

        // Act + Assert - via rule
        MysticLightAPI.initialize(nonExistentPath);
    }

    @Test
    public void testInitialize_directoryWithoutDll_nativeDllLoaded() throws Exception {
        // Arrange
        final Path path = Paths.get("C:\\");
        final Path expectedDllPath = path.resolve(getDllNameForSystemArchitecture());

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("The native DLL at %s does not exist.", expectedDllPath.toAbsolutePath().toString()));

        // Act + Assert - via rule
        MysticLightAPI.initialize(path);
    }

    @Test
    public void testInitialize_dllSpecified_nativeDllLoaded() throws Exception {
        // Arrange
        final String dllForArch = getDllNameForSystemArchitecture();
        final Path dllPath = Paths.get(System.getProperty("user.dir")).resolve(dllForArch);

        // Act
        MysticLightAPI.initialize(dllPath);

        // Assert
        assertNativeLibraryLoaded();
    }

    private void assertNativeLibraryLoaded() throws Exception {
        final Field clLibrariesField = ClassLoader.class.getDeclaredField("loadedLibraryNames");
        clLibrariesField.setAccessible(true);
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        final String expectedLoadedLibrary = getDllNameForSystemArchitecture();
        final Object field = clLibrariesField.get(classLoader);
        if (!(field instanceof Iterable)) {
            throw new IllegalStateException("Loaded libraries in class loader are not an iterable.");
        }

        final Iterable<?> libraries = (Iterable<?>) field;
        assertTrue(Streams.stream(libraries)
                .filter(l -> l instanceof String)
                .map(l -> (String) l)
                .anyMatch(lib -> lib.endsWith(expectedLoadedLibrary)));
    }

    private String getDllNameForSystemArchitecture() {
        final String osArch = System.getProperty("os.arch").toLowerCase(Locale.getDefault());
        return osArch.contains("x86") ? MysticLightAPI.NATIVE_DLL_NAME_X86 : MysticLightAPI.NATIVE_DLL_NAME_X64;
    }
}