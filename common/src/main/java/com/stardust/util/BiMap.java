package com.stardust.util;

import androidx.annotation.Keep;

import java.util.Map;
import java.util.Set;
@Keep

public interface BiMap<K, V> extends Map<K, V> {

    K getKey(V value);

    Set<V> valueSet();

    V getOr(K key, V def);

    K getKeyOr(V value, K def);

}
