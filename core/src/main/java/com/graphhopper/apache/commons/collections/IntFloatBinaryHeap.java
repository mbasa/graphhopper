/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.apache.commons.collections;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * This class is a partial copy of the class org.apache.commons.collections.BinaryHeap for
 * just the min heap and primitive, sorted float keys and associated int elements.
 * <p>
 * The library can be found here: https://commons.apache.org/proper/commons-collections/
 */
public class IntFloatBinaryHeap {
    private static final int GROW_FACTOR = 2;
    private int size;
    private int[] elements;
    private float[] keys;

    public IntFloatBinaryHeap() {
        this(1000);
    }

    public IntFloatBinaryHeap(int initialCapacity) {
        //+1 as element 0 is noop
        elements = new int[initialCapacity + 1];
        keys = new float[initialCapacity + 1];
        // make minimum to avoid zero array check in while loop
        keys[0] = Float.NEGATIVE_INFINITY;
    }

    private boolean isFull() {
        //+1 as element 0 is noop
        return elements.length == size + 1;
    }

    public void update(double key, int element) {
        int i;
        // we have no clue about the element order, so we need to search the full array
        for (i = 1; i <= size; i++) {
            if (elements[i] == element)
                break;
        }

        if (i > size)
            return;

        if (key > keys[i]) {
            keys[i] = (float) key;
            percolateDownMinHeap(i);
        } else {
            keys[i] = (float) key;
            percolateUpMinHeap(i);
        }
    }

    public void insert(double key, int element) {
        if (isFull()) {
            ensureCapacity(elements.length * GROW_FACTOR);
        }

        size++;
        elements[size] = element;
        keys[size] = (float) key;
        percolateUpMinHeap(size);
    }

    public int peekElement() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty. Cannot peek element.");
        } else {
            return elements[1];
        }
    }

    public float peekKey() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty. Cannot peek key.");
        else
            return keys[1];
    }

    public int poll() {
        final int result = peekElement();
        elements[1] = elements[size];
        keys[1] = keys[size];
        size--;

        if (size != 0)
            percolateDownMinHeap(1);

        return result;
    }

    /**
     * Percolates element down heap from the array position given by the index.
     */
    final void percolateDownMinHeap(final int index) {
        final int element = elements[index];
        final float key = keys[index];
        int hole = index;

        while (hole * 2 <= size) {
            int child = hole * 2;

            // if we have a right child and that child can not be percolated
            // up then move onto other child
            if (child != size && keys[child + 1] < keys[child]) {
                child++;
            }

            // if we found resting place of bubble then terminate search
            if (keys[child] >= key) {
                break;
            }

            elements[hole] = elements[child];
            keys[hole] = keys[child];
            hole = child;
        }

        elements[hole] = element;
        keys[hole] = key;
    }

    final void percolateUpMinHeap(final int index) {
        int hole = index;
        int element = elements[hole];
        float key = keys[hole];
        // parent == hole/2
        while (key < keys[hole / 2]) {
            final int next = hole / 2;
            elements[hole] = elements[next];
            keys[hole] = keys[next];
            hole = next;
        }
        elements[hole] = element;
        keys[hole] = key;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        trimTo(0);
    }

    void trimTo(int toSize) {
        this.size = toSize;
        toSize++;
        // necessary as we currently do not init arrays when inserting
        Arrays.fill(elements, toSize, size + 1, 0);
    }

    public void ensureCapacity(int capacity) {
        if (capacity < size) {
            throw new IllegalStateException("IntFloatBinaryHeap contains too many elements to fit in new capacity.");
        }

        elements = Arrays.copyOf(elements, capacity + 1);
        keys = Arrays.copyOf(keys, capacity + 1);
    }

    public long getCapacity() {
        return elements.length;
    }

    public long getMemoryUsage() {
        return elements.length * 4L + keys.length * 4L;
    }
}
