package de.matthiasfisch.mysticlight4j.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Exception thrown when the call of a Mystic Light API function failed. The method {@link #getMlApiErrorCode()}
 * returns the error code that was returned by the native function. The error message is the value returned by
 * {@code MLAPI_GetErrorMessage}.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public final class MysticLightAPIException extends RuntimeException {
    private final int mlApiErrorCode;
    public MysticLightAPIException(final String message, final int mlApiErrorCode) {
        super(message);
        this.mlApiErrorCode = mlApiErrorCode;
    }
}
