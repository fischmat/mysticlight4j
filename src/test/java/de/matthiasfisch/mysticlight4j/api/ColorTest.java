package de.matthiasfisch.mysticlight4j.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.internal.exceptions.EqualsVerifierInternalBugException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.rmi.server.ExportException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ColorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCTOR_redIsNegative_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Red value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) -1, (short) 1, (short) 1);
    }

    @Test
    public void testCTOR_greenIsNegative_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Green value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) 1, (short) -1, (short) 1);
    }

    @Test
    public void testCTOR_blueIsNegative_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Blue value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) 1, (short) 1, (short) -1);
    }

    @Test
    public void testCTOR_redIsAboveMax_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Red value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) 256, (short) 1, (short) 1);
    }

    @Test
    public void testCTOR_greenIsAboveMax_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Green value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) 1, (short) 256, (short) 1);
    }

    @Test
    public void testCTOR_blueIsAboveMax_illegalArgumentExceptionThrown() {
        // Arrange
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Blue value must be in range [0, 255].");

        // Act + Assert - via rule
        new Color((short) 1, (short) 1, (short) 256);
    }

    @Test
    public void testCTOR_allChannelsZero_correctlyConstructed() {
        // Arrange
        final short red = 0;
        final short green = 0;
        final short blue = 0;

        // Act
        final Color subject = new Color(red, green, blue);

        // Assert
        assertThat(subject.getRed(), is(red));
        assertThat(subject.getGreen(), is(green));
        assertThat(subject.getBlue(), is(blue));
    }

    @Test
    public void testCTOR_allChannelsMax_correctlyConstructed() {
        // Arrange
        final short red = 255;
        final short green = 255;
        final short blue = 255;

        // Act
        final Color subject = new Color(red, green, blue);

        // Assert
        assertThat(subject.getRed(), is(red));
        assertThat(subject.getGreen(), is(green));
        assertThat(subject.getBlue(), is(blue));
    }

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange + Act + Assert - via verifier
        EqualsVerifier.forClass(Color.class).verify();
    }
}