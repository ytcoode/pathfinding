package io.ytcode.pathfinding.astar;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static io.ytcode.pathfinding.astar.Point.toPoint;
import static io.ytcode.pathfinding.astar.Utils.check;

public class VisualDemo extends GridCanvas {

  public static void main(String[] args) {
    Grid map = new Grid(gridWidth, gridHeight);

    int x1 = 0;
    int y1 = 0;
    int x2 = map.getWidth() - 1;
    int y2 = map.getHeight() - 1;

    setWalkable(map, x1, y1, x2, y2, true);

    Path path = new AStar().search(x1, y1, x2, y2, map, false);
    new VisualDemo(map, path);
  }

  private static void setWalkable(Grid map, int x1, int y1, int x2, int y2, boolean random) {
    if (!random) {
      for (int y = 0; y < map.getHeight() - 1; y++) {
        map.setWalkable(map.getWidth() / 3, y, false);
      }
      for (int y = 1; y < map.getHeight(); y++) {
        map.setWalkable(map.getWidth() / 3 * 2, y, false);
      }
      return;
    }

    Random r = new Random();
    int n = map.getWidth() * map.getHeight() / 5;

    for (int i = 0; i < n; i++) {
      int x = r.nextInt(map.getWidth());
      int y = r.nextInt(map.getHeight());

      if (x == x1 && y == y1) {
        continue;
      }

      if (x == x2 && y == y2) {
        continue;
      }
      map.setWalkable(x, y, false);
    }
  }

  private final Set<Long> paintedPs = new HashSet<>();

  private final Grid map;
  private final Path path;

  private VisualDemo(Grid map, Path path) {
    super("A*");
    this.map = map;
    this.path = path;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    // 清除标志位
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

      int x = Point.getX(p);
      int y = Point.getY(p);
      fillGrid(g, x, y, color);

      if (lastP != -1) {
        drawLine(g, lastP, p);
      }
      lastP = p;
    }
  }

  private void drawLine(Graphics g, long p1, long p2) {
    int x1 = Point.getX(p1);
    int y1 = Point.getY(p1);
    int x2 = Point.getX(p2);
    int y2 = Point.getY(p2);
    drawLine(g, x1, y1, x2, y2, Color.GRAY);
  }

  @Override
  protected void fillGrid(Graphics g, int x, int y, Color c) {
    check(paintedPs.add(toPoint(x, y)));
    super.fillGrid(g, x, y, c);
  }
}
