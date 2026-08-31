package net.murfgames.rdloader.util;

import java.util.HashMap;

public class AccumulativeMap<K, V> extends HashMap<K, V> {
    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Removals are not allowed in this map.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clearing is not allowed in this map.");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("Removals are not allowed in this map.");
    }

    @Override
    public V replace(K key, V value) {
        throw new UnsupportedOperationException("Replacements are not allowed in this map.");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException("Replacements are not allowed in this map.");
    }
}
