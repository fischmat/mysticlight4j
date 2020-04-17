package de.matthiasfisch.mysticlight4j;

import org.junit.Assume;
import org.junit.Before;

import java.util.Locale;

import static org.junit.Assume.assumeTrue;

/**
 * Base class for all tests that may only be run on Windows operating systems.
 */
public abstract class WindowsOnlyTest {
    @Before
    public void setUp() {
        final String operatingSystem = System.getProperty("os.name").toLowerCase(Locale.getDefault());
        assumeTrue(operatingSystem.contains("windows"));
    }
}
