package de.matthiasfisch.mysticlight4j.api;

import lombok.EqualsAndHashCode;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class LedInfoTest {

    @Test
    public void testEqualsAndHashCode_withVerifier_verificationOk() {
        // Arrange + Act + Assert - via verifier
        EqualsVerifier.forClass(LedInfo.class).verify();
    }
}