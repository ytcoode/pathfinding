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
import static io.ytcode.pathfinding.astar.Node.setF;
import static io.ytcode.pathfinding.astar.Node.setG;

/** http://homepages.abdn.ac.uk/f.guerin/pages/teaching/CS1013/practicals/aStarTutorial.htm */
public class AStar {
  private static final Logger logger = LoggerFactory.getLogger(AStar.class);

  private final Nodes nodes;

  public AStar() {
    this.nodes = new Nodes();
  }

  public Path search(int sx, int sy, int ex, int ey, Grid map) {
    Path p = new Path();
    search(sx, sy, ex, ey, map, p);
    return p;
  }

  public void search(int sx, int sy, int ex, int ey, Grid map, Path path) {
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
          fillPath(x, y, path, sx, sy, map);
          return;
        }

        int pg = getG(n);

        open(x, y - 1, pg + COST_ORTHOGONAL, DIRECTION_UP, ex, ey, map);
        open(x, y + 1, pg + COST_ORTHOGONAL, DIRECTION_DOWN, ex, ey, map);
        open(x + 1, y, pg + COST_ORTHOGONAL, DIRECTION_LEFT, ex, ey, map);
        open(x - 1, y, pg + COST_ORTHOGONAL, DIRECTION_RIGHT, ex, ey, map);
        open(x + 1, y - 1, pg + COST_DIAGONAL, DIRECTION_LEFT_UP, ex, ey, map);
        open(x + 1, y + 1, pg + COST_DIAGONAL, DIRECTION_LEFT_DOWN, ex, ey, map);
        open(x - 1, y - 1, pg + COST_DIAGONAL, DIRECTION_RIGHT_UP, ex, ey, map);
        open(x - 1, y + 1, pg + COST_DIAGONAL, DIRECTION_RIGHT_DOWN, ex, ey, map);
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
    if (!map.isWalkable(x, y)) {
      return;
    }

    int ni = map.nodeInfo(x, y);

    if (isNullNode(ni)) {
      nodes.open(x, y, g, hCost(x, y, ex, ey), pd);
      return;
    }

    if (isClosedNode(ni)) {
      return;
    }

    int idx = openNodeIdx(ni);
    long n = nodes.getOpenNode(idx);

    int ng = getG(n);
    if (g >= ng) {
      return;
    }

    n = setF(n, getF(n) - ng + g);
    n = setG(n, g);

    nodes.openNodeParentChanged(n, idx, pd);
  }

  private void fillPath(int x, int y, Path path, int sx, int sy, Grid map) {
    path.add(x, y);
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
        path.add(x, y);
        return;
      }

      int ppd = map.nodeParentDirection(x, y);
      if (ppd != pd) {
        path.add(x, y);
        pd = ppd;
      }
    }
  }

  private void clear() {
    nodes.clear();
  }

  private boolean isCLean(Grid map) { // for test
    return nodes.isClean() && map.isClean();
  }
}
