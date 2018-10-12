package io.ytcode.pathfinding.astar;

import static io.ytcode.pathfinding.astar.Utils.mask;

class Node {
  //  int x, y;
  //  int g, f;

  private static final int F_BITS = 16;
  private static final int F_MASK = mask(F_BITS);
  private static final int F_SHIFT = 0;
  private static final long F_SHIFT_MASK = (long) F_MASK << F_SHIFT;

  private static final int G_BITS = 16;
  private static final int G_MASK = mask(G_BITS);
  private static final int G_SHIFT = F_BITS;
  private static final long G_SHIFT_MASK = (long) G_MASK << G_SHIFT;

  private static final int Y_BITS = 16;
  static final int Y_MASK = mask(Y_BITS);
  private static final int Y_SHIFT = G_BITS + F_BITS;
  private static final long Y_SHIFT_MASK = (long) Y_MASK << Y_SHIFT;

  private static final int X_BITS = 16;
  static final int X_MASK = mask(X_BITS);
  private static final int X_SHIFT = Y_BITS + G_BITS + F_BITS;
  private static final long X_SHIFT_MASK = (long) X_MASK << X_SHIFT;

  static long toNode(int x, int y, int g, int f) {
    if (f < 0 || f > F_MASK) { // 如果这里报错，Cost类里改成2:3? 或者保存h而不是f?
      throw new RuntimeException("TooBigF");
    }
    return (long) x << X_SHIFT | (long) y << Y_SHIFT | g << G_SHIFT | f;
  }

  static long setX(long l, int v) {
    assert v >= 0 && v <= X_MASK;
    return set(l, v, X_SHIFT_MASK, X_SHIFT);
  }

  static int getX(long l) {
    return get(l, X_MASK, X_SHIFT);
  }

  static long setY(long l, int v) {
    assert v >= 0 && v <= Y_MASK;
    return set(l, v, Y_SHIFT_MASK, Y_SHIFT);
  }

  static int getY(long l) {
    return get(l, Y_MASK, Y_SHIFT);
  }

  static long setG(long l, int v) {
    assert v >= 0 && v <= G_MASK;
    return set(l, v, G_SHIFT_MASK, G_SHIFT);
  }

  static int getG(long l) {
    return get(l, G_MASK, G_SHIFT);
  }

  static long setF(long l, int v) {
    if (v < 0 || v > F_MASK) { // 如果这里报错，Cost类里改成2:3? 或者保存h而不是f?
      throw new RuntimeException("TooBigF");
    }
    return set(l, v, F_SHIFT_MASK, F_SHIFT);
  }

  static int getF(long l) {
    return get(l, F_MASK, F_SHIFT);
  }

  private static long set(long l, int v, long shiftMask, int shift) {
    return l & ~shiftMask | ((long) v << shift);
  }

  private static int get(long l, int mask, int shift) {
    return (int) (l >>> shift & mask);
  }
}
