package io.ytcode.pathfinding.astar;

import static io.ytcode.pathfinding.astar.Point.toPoint;

public class Reachability {

  public static boolean isReachable(int x1, int y1, int x2, int y2, Grid grid) {
    return isReachable(x1, y1, x2, y2, 1, grid);
  }

  public static boolean isReachable(int x1, int y1, int x2, int y2, int scale, Grid grid) {
    return getClosestWalkablePointToTarget(x1, y1, x2, y2, scale, grid) == toPoint(x2, y2);
  }

  public static long getClosestWalkablePointToTarget(int x1, int y1, int x2, int y2, Grid grid) {
    return getClosestWalkablePointToTarget(x1, y1, x2, y2, 1, grid);
  }

  public static long getClosestWalkablePointToTarget(
      int x1, int y1, int x2, int y2, int scale, Grid grid) {
    if (scale < 1) {
      throw new IllegalArgumentException("Illegal scale: " + scale);
    }

    double cx1 = scaleDown(x1 + 0.5, scale);
    double cy1 = scaleDown(y1 + 0.5, scale);

    double cx2 = scaleDown(x2 + 0.5, scale);
    double cy2 = scaleDown(y2 + 0.5, scale);

    int gx1 = (int) cx1;
    int gy1 = (int) cy1;

    // 起始格就不可行走
    if (!grid.isWalkable(gx1, gy1)) {
      return toPoint(x1, y1);
    }

    int gx2 = (int) cx2;
    int gy2 = (int) cy2;

    // 在同一格
    if (gx1 == gx2 && gy1 == gy2) {
      return toPoint(x2, y2);
    }

    // 水平直线
    if (y1 == y2) { // 绝对水平
      int inc = gx2 > gx1 ? 1 : -1;
      for (int gx = gx1 + inc; ; gx += inc) {
        if (!grid.isWalkable(gx, gy1)) {
          if (gx - inc == gx1) { // 第二格就不可走了，返回起始点
            return toPoint(x1, y1);
          }
          return toPoint(scaleUp(gx - inc, scale), y1); // 中间某一格不可行走了，保留y轴
        }
        if (gx == gx2) {
          return toPoint(x2, y2);
        }
      }
    }

    // 竖直直线
    if (x1 == x2) { // 绝对竖直
      int inc = gy2 > gy1 ? 1 : -1;
      for (int gy = gy1 + inc; ; gy += inc) {
        if (!grid.isWalkable(gx1, gy)) {
          if (gy - inc == gy1) {
            return toPoint(x1, y1);
          }
          return toPoint(x1, scaleUp(gy - inc, scale));
        }
        if (gy == gy2) {
          return toPoint(x2, y2);
        }
      }
    }

    // 斜线的情况
    // y=k*x+b, k=dy/dx, b=y-k*x

    double dx = cx2 - cx1;
    double dy = cy2 - cy1;

    double k = dy / dx;
    double b = cy1 - k * cx1;

    double deltaX, deltaY;
    if (Math.abs(dx) > Math.abs(dy)) { // 偏x轴，递增x
      deltaX = dx > 0 ? 1 : -1;
      deltaY = deltaX * k;
    } else { // 偏y轴，递增y
      deltaY = dy > 0 ? 1 : -1;
      deltaX = deltaY / k;
    }

    while (true) {
      cx1 += deltaX;
      cy1 += deltaY;

      int gx = (int) cx1;
      int gy = (int) cy1;

      if (!grid.isWalkable(gx, gy)) {
        break;
      }

      if (gx != gx1 && gy != gy1) { // 格子的xy坐标都变了
        int x0 = dx > 0 ? gx : gx1;
        double y0 = k * x0 + b; // x为该格子的起始点时，y的坐标

        // 如果y0正好是个整数，表明该直线正好穿过格子的交叉点
        // 而当k>0，说明该直线又是个左下右上直线
        // 这种情况，该交叉点是属于右上格子额，所以没穿过其他格子，不用多余判断
        if (Math.rint(y0) != y0 || k < 0) {
          int gy0 = (int) y0;
          if (gy0 == gy) {
            if (!grid.isWalkable(gx1, gy)) {
              break;
            }
          } else {
            if (!grid.isWalkable(gx, gy1)) {
              break;
            }
          }
        }
      }

      if (gx == gx2 && gy == gy2) {
        return toPoint(x2, y2);
      }

      gx1 = gx;
      gy1 = gy;
    }

    // 因不可行走导致中断，倒退回上一个检查点并返回
    return scaleUpPoint(cx1 - deltaX, cy1 - deltaY, scale);
  }

  private static double scaleDown(double d, int scale) {
    return d / scale;
  }

  private static int scaleUp(int i, int scale) {
    return i * scale + scale / 2; // 大格的中心点
  }

  private static int scaleUp(double d, int scale) {
    return (int) (d * scale);
  }

  private static long scaleUpPoint(int x, int y, int scale) {
    return toPoint(scaleUp(x, scale), scaleUp(y, scale));
  }

  private static long scaleUpPoint(double x, double y, int scale) {
    return toPoint(scaleUp(x, scale), scaleUp(y, scale));
  }
}
