package io.ytcode.pathfinding.astar;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static io.ytcode.pathfinding.astar.Point.toPoint;
import static io.ytcode.pathfinding.astar.Sketchpad.gridHeight;
import static io.ytcode.pathfinding.astar.Sketchpad.gridWidth;
import static io.ytcode.pathfinding.astar.Utils.check;

public class AStarTest {

  private static final boolean benchmark = false;

  private static final Grid map = buildMap();

  private static final int x1 = 0;
  private static final int y1 = 0;
  private static final int x2 = map.getWidth() - 1;
  private static final int y2 = map.getHeight() - 1;

  static {
    if (!benchmark) {
      for (int i = 0; i < 200; i++) {
        int x = ThreadLocalRandom.current().nextInt(map.getWidth());
        int y = ThreadLocalRandom.current().nextInt(map.getHeight());
        if (x == x1 && y == y1) {
          continue;
        }
        if (x == x2 && y == y2) {
          continue;
        }
        map.setWalkable(x, y, false);
      }
    }
  }

  public static void main(String[] args) {
    Path path = ThreadLocalAStar.current().search(x1, y1, x2, y2, map);
    if (benchmark) {
      benchmark(path);
    } else {
      paint(path);
    }
  }

  private static Grid buildMap() {
    Grid map;
    if (benchmark) {
      map = new Grid(300, 300);
    } else {
      map = new Grid(gridWidth, gridHeight);
    }
    return map;
  }

  private static void benchmark(Path p1) {
    Path p2 = new Path();
    int n = 50000;
    long st;

    for (int k = 0; k < 5; k++) {
      st = System.currentTimeMillis();
      for (int i = 0; i < n; i++) {
        ThreadLocalAStar.current().search(x1, y1, x2, y2, map, p2);
        check(p1.size() == p2.size());
      }
      System.out.println("dt: " + (System.currentTimeMillis() - st));
    }
  }

  private static void paint(Path path) {
    new Sketchpad("Hello") {

      private final Set<Long> paintedPs = new HashSet<>();

      @Override
      public void paint(Graphics g) {
        super.paint(g);

        // 清楚标志位
        paintedPs.clear();

        // 画阻挡
        for (int i = 0; i < map.getWidth(); i++) {
          for (int j = 0; j < map.getHeight(); j++) {
            if (!map.isWalkable(i, j)) {
              fillGrid(g, i, j, Color.BLACK);
            }
          }
        }

        // 画路径
        long lastP = -1;
        for (int i = 0; i < path.size(); i++) {
          Color color;
          if (i == 0) {
            color = Color.GREEN;
          } else if (i == path.size() - 1) {
            color = Color.RED;
          } else {
            color = Color.BLUE;
          }

          long p = path.get(i);

          int x = Point.X(p);
          int y = Point.Y(p);
          fillGrid(g, x, y, color);

          if (lastP != -1) {
            drawLine(g, lastP, p);
          }
          lastP = p;
        }
      }

      private void drawLine(Graphics g, long p1, long p2) {
        int x1 = Point.X(p1);
        int y1 = Point.Y(p1);
        int x2 = Point.X(p2);
        int y2 = Point.Y(p2);
        drawLine(g, x1, y1, x2, y2, Color.GRAY);
      }

      @Override
      protected void fillGrid(Graphics g, int x, int y, Color c) {
        check(paintedPs.add(toPoint(x, y)));
        super.fillGrid(g, x, y, c);
      }
    };
  }
}
