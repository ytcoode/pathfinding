package io.ytcode.pathfinding.astar;

import java.util.Arrays;

import static io.ytcode.pathfinding.astar.Point.toPoint;

public class Path {

  private long[] ps;
  private int size;

  public Path() {
    this.ps = new long[8];
  }

  public long get(int i) {
    assert i >= 0 && i < size;
    return ps[size - 1 - i];
  }

  public int size() {
    return size;
  }

  void add(int x, int y) {
    long p = toPoint(x, y);
    if (size >= ps.length) {
      grow(size + 1);
    }
    ps[size] = p;
    size++;
  }

  void clear() {
    size = 0;
  }

  private void grow(int minCapacity) {
    int oldCapacity = ps.length;
    int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));

    if (newCapacity < minCapacity) {
      newCapacity = minCapacity;
    }

    if (newCapacity < 0) {
      throw new RuntimeException("Overflow");
    }
    ps = Arrays.copyOf(ps, newCapacity);
  }
}
