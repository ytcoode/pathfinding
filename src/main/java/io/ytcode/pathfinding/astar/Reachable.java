package io.ytcode.pathfinding.astar;

class Reachable {
  private static final double DELTA = 1.0;

  static boolean isReachable(
      int x1, int y1, int x2, int y2, Grid map) { // 必须使用double，而不能是float，保证int范围内的数的精度
    assert map.isWalkable(x1, y1) && map.isWalkable(x2, y2);

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
      return true;
    }

    double cx1 = x1 + 0.5; // 起始中心点
    double cy1 = y1 + 0.5;

    double cx2 = x2 + 0.5; // 终点中心点
    double cy2 = y2 + 0.5;

    // 45度角斜线
    if (Math.abs(dx) == Math.abs(dy)) {
      double deltaX = dx > 0 ? DELTA : -DELTA;
      double deltaY = dy > 0 ? DELTA : -DELTA;

      do {
        cx1 += deltaX;
        cy1 += deltaY;

        if (!map.isWalkable((int) cx1, (int) cy1)) {
          return false;
        }

        if (dx > 0 && dy < 0) { // 往右下角走
          if (!map.isWalkable((int) cx1, (int) (cy1 - deltaY))) {
            return false;
          }
        } else if (dx < 0 && dy > 0) { // 往左上角走
          if (!map.isWalkable((int) (cx1 - deltaX), (int) cy1)) {
            return false;
          }
        }
      } while (cx1 != cx2 && cy1 != cy2);

      return true;
    }

    // 偏x轴，递增x
    if (Math.abs(dx) > Math.abs(dy)) {
      double deltaX = dx > 0 ? DELTA : -DELTA;
      double deltaY = (double) dy / dx * deltaX;

      int lastY = y1;

      do {
        cx1 += deltaX;
        cy1 += deltaY;

        int x = (int) cx1;
        int y = (int) cy1;
        if (!map.isWalkable(x, y)) {
          return false;
        }

        // x轴每次进一格，y轴每次进小于一格
        if (y != lastY) {
          int lineY = (int) (cy1 - deltaY / 2); // 边界线时的y
          if (lastY == lineY) { // 穿越底格跨格
            if (!map.isWalkable(x, lastY)) {
              return false;
            }
          } else {
            assert y == lineY;
            if (!map.isWalkable((int) (cx1 - deltaX), y)) {
              return false;
            }
          }
          lastY = y;
        }
      } while (cx1 != cx2);

      return true;
    }

    // 偏y轴，递增y
    double deltaY = dy > 0 ? DELTA : -DELTA;
    double deltaX = (double) dx / dy * deltaY;

    int lastX = x1;

    do {
      cx1 += deltaX;
      cy1 += deltaY;

      int x = (int) cx1;
      int y = (int) cy1;
      if (!map.isWalkable(x, y)) {
        return false;
      }

      if (x != lastX) {
        int lineX = (int) (cx1 - deltaX / 2); // 边界线时的x
        if (lastX == lineX) { // 穿越底格跨格
          if (!map.isWalkable(lastX, y)) {
            return false;
          }
        } else {
          assert x == lineX;
          if (!map.isWalkable(x, (int) (cy1 - deltaY))) {
            return false;
          }
        }
        lastX = x;
      }
    } while (cy1 != cy2);

    return true;
  }
}
