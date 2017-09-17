package de.geosearchef.rtsIO.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<K, V> {
    private K first;
    private V second;
}
