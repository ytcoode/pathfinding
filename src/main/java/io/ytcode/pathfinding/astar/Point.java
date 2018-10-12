package io.ytcode.pathfinding.astar;

public class Point {

  static int X(long p) {
    return (int) (p >>> 32);
  }

  public static int Y(long p) {
    return (int) p;
  }

  public static long toPoint(int x, int y) {
    return (((long) x) << 32) | (y & 0xFFFFFFFFL);
  }
}
