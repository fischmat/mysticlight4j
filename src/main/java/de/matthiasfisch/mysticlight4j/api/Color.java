package de.matthiasfisch.mysticlight4j.api;

import lombok.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class Color {
    private final byte red;
    private final byte green;
    private final byte blue;
}
