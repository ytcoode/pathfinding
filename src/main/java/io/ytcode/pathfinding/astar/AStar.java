package io.ytcode.pathfinding.astar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.ytcode.pathfinding.astar.Cost.COST_DIAGONAL;
import static io.ytcode.pathfinding.astar.Cost.COST_ORTHOGONAL;
import static io.ytcode.pathfinding.astar.Cost.hCost;
import static io.ytcode.pathfinding.astar.Grid.*;
import static io.ytcode.pathfinding.astar.Node.getF;
import static io.ytcode.pathfinding.astar.Node.getG;
import static io.ytcode.pathfinding.astar.Node.getX;
import static io.ytcode.pathfinding.astar.Node.getY;
import static io.ytcode.pathfinding.astar.Node.setGF;
import static java.lang.Math.max;
import static java.lang.Math.min;

/** http://homepages.abdn.ac.uk/f.guerin/pages/teaching/CS1013/practicals/aStarTutorial.htm */
public class AStar {
  private static final Logger logger = LoggerFactory.getLogger(AStar.class);
  private static final float DELTA = 1f;

  private final Nodes nodes;

  public AStar() {
    this.nodes = new Nodes();
  }

  public Path search(int sx, int sy, int ex, int ey, Grid map) {
    return search(sx, sy, ex, ey, map, false);
  }

  public Path search(int sx, int sy, int ex, int ey, Grid map, boolean smooth) {
    Path p = new Path();
    search(sx, sy, ex, ey, map, p, smooth);
    return p;
  }

  public void search(int sx, int sy, int ex, int ey, Grid map, Path path) {
    search(sx, sy, ex, ey, map, path, false);
  }

  public void search(
      int sx, int sy, int ex, int ey, Grid map, Path path, boolean smooth) { // smooth出来的路径不保证完全准确
    assert isCLean(map);
    if (!map.isWalkable(sx, sy)) {
      return;
    }

    if (!map.isWalkable(ex, ey)) {
      return;
    }

    if (sx == ex && sy == ey) {
      return;
    }

    path.clear();

    int endX = map.getWidth() - 1;
    int endY = map.getHeight() - 1;

    try {
      nodes.map = map; // 必须放在开始
      nodes.open(sx, sy, 0, hCost(sx, sy, ex, ey), DIRECTION_UP); // 起始点的方向不会被用到

      while (true) {
        long n = nodes.close();
        if (n == 0) {
          return;
        }

        int x = getX(n);
        int y = getY(n);

        if (x == ex && y == ey) {
          fillPath(x, y, path, sx, sy, map, smooth);
          return;
        }

        int pg = getG(n);

        int x1 = max(x - 1, 0);
        int x2 = min(x + 1, endX);
        int y1 = max(y - 1, 0);
        int y2 = min(y + 1, endY);

        open(x, y1, pg + COST_ORTHOGONAL, DIRECTION_UP, ex, ey, map);
        open(x, y2, pg + COST_ORTHOGONAL, DIRECTION_DOWN, ex, ey, map);
        open(x2, y, pg + COST_ORTHOGONAL, DIRECTION_LEFT, ex, ey, map);
        open(x1, y, pg + COST_ORTHOGONAL, DIRECTION_RIGHT, ex, ey, map);
        open(x2, y1, pg + COST_DIAGONAL, DIRECTION_LEFT_UP, ex, ey, map);
        open(x2, y2, pg + COST_DIAGONAL, DIRECTION_LEFT_DOWN, ex, ey, map);
        open(x1, y1, pg + COST_DIAGONAL, DIRECTION_RIGHT_UP, ex, ey, map);
        open(x1, y2, pg + COST_DIAGONAL, DIRECTION_RIGHT_DOWN, ex, ey, map);
      }
    } catch (Exception e) {
      logger.error("AStar.search: from {}-{} to {}-{}", sx, sy, ex, ey, e);
      path.clear();
    } finally {
      clear();
      assert isCLean(map);
    }
  }

  private void open(int x, int y, int g, int pd, int ex, int ey, Grid map) {
    int info = map.info(x, y);

    if (isUnwalkable(info)) {
      return;
    }

    /*
     * 方向是直角坐标系第一象限中的方向，即0,0点在左下角
     * 所以一个格子的坐下角点，左边，下边都属于这个格子，而上边，右边及右上角点都属于其他格子
     *
     * 假设在网格坐标系中，一个格子大小是100*100
     *
     * 点(0,0)是(0,0)格子的左下角点
     * 边(0,0-99)是(0,0)格子的左边
     * 边(0-99,0)是(0,0)格子的下边
     *
     * 点(100,100)是(1,1)格子的左下角点
     * 边(0-99,100)是(0,1)格子的下边
     * 边(100,0-99)是(1,0)格子的右边
     */
    switch (pd) {
      case DIRECTION_RIGHT_DOWN: // 往左上走
        if (!map.isWalkable(x + 1, y)) {
          return;
        }
        break;

      case DIRECTION_LEFT_UP: // 往右下走
        if (!map.isWalkable(x, y + 1)) {
          return;
        }
        break;
    }

    if (isNullNode(info)) {
      nodes.open(x, y, g, hCost(x, y, ex, ey), pd);
      return;
    }

    if (isClosedNode(info)) {
      return;
    }

    int idx = openNodeIdx(info);
    long n = nodes.getOpenNode(idx);

    int ng = getG(n);
    if (g >= ng) {
      return;
    }

    n = setGF(n, g, getF(n) - ng + g);
    nodes.openNodeParentChanged(n, idx, pd);
  }

  private void fillPath(int x, int y, Path path, int sx, int sy, Grid map, boolean smooth) {
    pathAdd(path, x, y, map, smooth);
    int pd = map.nodeParentDirection(x, y);

    while (true) {
      switch (pd) {
        case DIRECTION_UP:
          y++;
          break;

        case DIRECTION_DOWN:
          y--;
          break;

        case DIRECTION_LEFT:
          x--;
          break;

        case DIRECTION_RIGHT:
          x++;
          break;

        case DIRECTION_LEFT_UP:
          x--;
          y++;
          break;

        case DIRECTION_LEFT_DOWN:
          x--;
          y--;
          break;

        case DIRECTION_RIGHT_UP:
          x++;
          y++;
          break;

        case DIRECTION_RIGHT_DOWN:
          x++;
          y--;
          break;

        default:
          throw new RuntimeException("illegal direction: " + pd);
      }

      if (x == sx && y == sy) {
        pathAdd(path, x, y, map, smooth);
        return;
      }

      int ppd = map.nodeParentDirection(x, y);
      if (ppd != pd) {
        pathAdd(path, x, y, map, smooth);
        pd = ppd;
      }
    }
  }

  private void pathAdd(Path path, int x, int y, Grid map, boolean smooth) {
    if (!smooth) {
      path.add(x, y);
      return;
    }

    // 这个点到上一个点是直达的，所以只用看上上个点
    while (path.size() >= 2) {
      long p = path.get(1);
      int x2 = Point.getX(p);
      int y2 = Point.getY(p);

      if (!canReachDirectly(x, y, x2, y2, map)) {
        path.add(x, y);
        return;
      }
      path.remove();
    }
    path.add(x, y);
  }

  private boolean canReachDirectly(int x1, int y1, int x2, int y2, Grid map) { // 不完全准确，只是近似
    int dx = x2 - x1;
    int dy = y2 - y1;

    // 水平直线
    if (dy == 0) {
      if (x2 > x1) {
        for (int x = x1 + 1; x < x2; x++) {
          if (!map.isWalkable(x, y1)) {
            return false;
          }
        }
      } else {
        for (int x = x2 + 1; x < x1; x++) {
          if (!map.isWalkable(x, y1)) {
            return false;
          }
        }
      }
      return true;
    }

    // 竖直直线
    if (dx == 0) {
      if (y2 > y1) {
        for (int y = y1 + 1; y < y2; y++) {
          if (!map.isWalkable(x1, y)) {
            return false;
          }
        }
      } else {
        for (int y = y2 + 1; y < y1; y++) {
          if (!map.isWalkable(x1, y)) {
            return false;
          }
        }
      }
    }

    // 偏x轴，递增x
    if (Math.abs(dx) > Math.abs(dy)) {
      float deltaX = dx > 0 ? DELTA : -DELTA;
      float deltaY = (float) dy / dx * deltaX;

      float x = x1 + 0.5f + deltaX; // 从起始点的中心开始
      float y = y1 + 0.5f + deltaY;

      if (dx > 0) {
        while (x < x2) {
          if (!map.isWalkable((int) x, (int) y)) {
            return false;
          }
          x += deltaX;
          y += deltaY;
        }
      } else {
        while (x > x2) {
          if (!map.isWalkable((int) x, (int) y)) {
            return false;
          }
          x += deltaX;
          y += deltaY;
        }
      }

      return true;
    }

    // 偏y轴，递增y
    float deltaY = dy > 0 ? DELTA : -DELTA;
    float deltaX = (float) dx / dy * deltaY;

    float x = x1 + 0.5f + deltaX; // 从起始点的中心开始
    float y = y1 + 0.5f + deltaY;

    if (dy > 0) {
      while (y < y2) {
        if (!map.isWalkable((int) x, (int) y)) {
          return false;
        }
        x += deltaX;
        y += deltaY;
      }
    } else {
      while (y > y2) {
        if (!map.isWalkable((int) x, (int) y)) {
          return false;
        }
        x += deltaX;
        y += deltaY;
      }
    }
    return true;
  }

  private void clear() {
    nodes.clear();
  }

  private boolean isCLean(Grid map) { // for test
    return nodes.isClean() && map.isClean();
  }
}
