package org.asteriskjava.lock;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class LockableList<S> extends Lockable implements List<S> {
    final List<S> list;

    public LockableList(List<S> list) {
        if (list instanceof Lockable) {
            throw new RuntimeException("list is already lockable");
        }
        this.list = list;
    }

    public void forEach(Consumer<? super S> action) {
        list.forEach(action);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public Iterator<S> iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(S e) {
        return list.add(e);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends S> c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends S> c) {
        return list.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    public void replaceAll(UnaryOperator<S> operator) {
        list.replaceAll(operator);
    }

    public boolean removeIf(Predicate<? super S> filter) {
        return list.removeIf(filter);
    }

    public void sort(Comparator<? super S> c) {
        list.sort(c);
    }

    public void clear() {
        list.clear();
    }

    public boolean equals(Object o) {
        return list.equals(o);
    }

    public int hashCode() {
        return list.hashCode();
    }

    public S get(int index) {
        return list.get(index);
    }

    public S set(int index, S element) {
        return list.set(index, element);
    }

    public void add(int index, S element) {
        list.add(index, element);
    }

    public Stream<S> stream() {
        return list.stream();
    }

    public S remove(int index) {
        return list.remove(index);
    }

    public Stream<S> parallelStream() {
        return list.parallelStream();
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<S> listIterator() {
        return list.listIterator();
    }

    public ListIterator<S> listIterator(int index) {
        return list.listIterator(index);
    }

    public List<S> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Spliterator<S> spliterator() {
        return list.spliterator();
    }

}
