package org.asteriskjava.lock;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LockableMap<S, P> extends Lockable implements Map<S, P>
{
    final Map<S, P> map;

    public LockableMap(Map<S, P> map)
    {
        if (map instanceof Lockable)
        {
            throw new RuntimeException("map is already lockable");
        }
        this.map = map;
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    public P get(Object key)
    {
        return map.get(key);
    }

    public P put(S key, P value)
    {
        return map.put(key, value);
    }

    public P remove(Object key)
    {
        return map.remove(key);
    }

    public void putAll(Map< ? extends S, ? extends P> m)
    {
        map.putAll(m);
    }

    public void clear()
    {
        map.clear();
    }

    public Set<S> keySet()
    {
        return map.keySet();
    }

    public Collection<P> values()
    {
        return map.values();
    }

    public Set<Entry<S, P>> entrySet()
    {
        return map.entrySet();
    }

    public boolean equals(Object o)
    {
        return map.equals(o);
    }

    public int hashCode()
    {
        return map.hashCode();
    }

    public P getOrDefault(Object key, P defaultValue)
    {
        return map.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer< ? super S, ? super P> action)
    {
        map.forEach(action);
    }

    public void replaceAll(BiFunction< ? super S, ? super P, ? extends P> function)
    {
        map.replaceAll(function);
    }

    public P putIfAbsent(S key, P value)
    {
        return map.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value)
    {
        return map.remove(key, value);
    }

    public boolean replace(S key, P oldValue, P newValue)
    {
        return map.replace(key, oldValue, newValue);
    }

    public P replace(S key, P value)
    {
        return map.replace(key, value);
    }

    public P computeIfAbsent(S key, Function< ? super S, ? extends P> mappingFunction)
    {
        return map.computeIfAbsent(key, mappingFunction);
    }

    public P computeIfPresent(S key, BiFunction< ? super S, ? super P, ? extends P> remappingFunction)
    {
        return map.computeIfPresent(key, remappingFunction);
    }

    public P compute(S key, BiFunction< ? super S, ? super P, ? extends P> remappingFunction)
    {
        return map.compute(key, remappingFunction);
    }

    public P merge(S key, P value, BiFunction< ? super P, ? super P, ? extends P> remappingFunction)
    {
        return map.merge(key, value, remappingFunction);
    }

}
