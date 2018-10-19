package io.ytcode.pathfinding.astar;

class Reachable {
  private static final float DELTA = 1f;

  static boolean isReachable(int x1, int y1, int x2, int y2, Grid map) {
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

    int dxAbs = Math.abs(dx);
    int dyAbs = Math.abs(dy);

    float cx1 = x1 + 0.5f; // 起始中心点
    float cy1 = y1 + 0.5f;

    float cx2 = x2 + 0.5f; // 终点中心点
    float cy2 = y2 + 0.5f;

    // 45度角斜线
    if (dxAbs == dyAbs) {
      float deltaX = dx > 0 ? DELTA : -DELTA;
      float deltaY = dy > 0 ? DELTA : -DELTA;

      float cx = cx1;
      float cy = cy1;

      do {
        cx += deltaX;
        cy += deltaY;

        if (!map.isWalkable((int) cx, (int) cy)) {
          return false;
        }

        if (dx > 0 && dy < 0) { // 往右下角走
          if (!map.isWalkable((int) cx, (int) (cy - deltaY))) {
            return false;
          }
        } else if (dx < 0 && dy > 0) { // 往左上角走
          if (!map.isWalkable((int) (cx - deltaX), (int) cy)) {
            return false;
          }
        }
      } while (cx != cx2 && cy != cy2);

      return true;
    }

    float cx = cx1; // 从起始点的中心开始
    float cy = cy1;

    // 偏x轴，递增x
    if (dxAbs > dyAbs) {
      float deltaX = dx > 0 ? DELTA : -DELTA;
      float deltaY = (float) dy / dx * deltaX;

      int lastY = y1;

      do {
        cx += deltaX;
        cy += deltaY;

        int x = (int) cx;
        int y = (int) cy;
        if (!map.isWalkable(x, y)) {
          return false;
        }

        // x轴每次进一格，y轴每次进小于一格
        if (y != lastY) {
          int lineY = (int) (cy - deltaY / 2); // 边界线时的y
          if (lastY == lineY) { // 穿越底格跨格
            if (!map.isWalkable(x, lastY)) {
              return false;
            }
          } else {
            assert y == lineY;
            if (!map.isWalkable((int) (cx - deltaX), y)) {
              return false;
            }
          }
          lastY = y;
        }

      } while (cx != cx2);

      return true;
    }

    // 偏y轴，递增y
    float deltaY = dy > 0 ? DELTA : -DELTA;
    float deltaX = (float) dx / dy * deltaY;

    int lastX = x1;

    do {
      cx += deltaX;
      cy += deltaY;

      int x = (int) cx;
      int y = (int) cy;
      if (!map.isWalkable(x, y)) {
        return false;
      }

      if (x != lastX) {
        int lineX = (int) (cx - deltaX / 2); // 边界线时的x
        if (lastX == lineX) { // 穿越底格跨格
          if (!map.isWalkable(lastX, y)) {
            return false;
          }
        } else {
          assert x == lineX;
          if (!map.isWalkable(x, (int) (cy - deltaY))) {
            return false;
          }
        }
        lastX = x;
      }

    } while (cy != cy2);

    return true;
  }
}
