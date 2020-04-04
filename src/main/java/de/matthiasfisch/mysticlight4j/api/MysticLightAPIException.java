package de.matthiasfisch.mysticlight4j.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class MysticLightAPIException extends RuntimeException {
    private final int mlApiErrorCode;
    public MysticLightAPIException(String message, int mlApiErrorCode) {
        super(message);
        this.mlApiErrorCode = mlApiErrorCode;
    }
}
