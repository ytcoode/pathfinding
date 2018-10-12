package io.ytcode.pathfinding.astar;

public class Benchmark {

  public static Path path = new Path();

  public static void main(String[] args) {
    AStar aStar = new AStar();
    for (int i = 1; i <= 10; i++) {
      Grid map = new Grid(i * 100, i * 100);
      benchmark(aStar, map);
    }
  }

  private static void benchmark(AStar aStar, Grid map) {
    int x1 = 0;
    int y1 = 0;
    int x2 = map.getWidth() - 1;
    int y2 = map.getHeight() - 1;
    int n = 100000;

    for (int i = 0; i < 3; i++) {
      long st = System.currentTimeMillis();
      for (int j = 0; j < n; j++) {
        aStar.search(x1, y1, x2, y2, map, path);
      }
      double dt = (System.currentTimeMillis() - st) / 1000d;
      int ops = (int) Math.round(n / dt);
      System.out.println(
          "From " + x1 + "-" + y1 + " to " + x2 + "-" + y2 + " -> " + ops + " ops/sec");
    }
    System.out.println();
  }
}
