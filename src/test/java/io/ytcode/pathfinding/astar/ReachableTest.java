package io.ytcode.pathfinding.astar;

import org.junit.jupiter.api.Test;

import static io.ytcode.pathfinding.astar.GridCanvas.gridHeight;
import static io.ytcode.pathfinding.astar.GridCanvas.gridWidth;

public class ReachableTest {

  @Test
  void isReachable() {
    Grid grid = new Grid(gridWidth, gridHeight);
    grid.setWalkable(1, 1, false);
    grid.setWalkable(2, 0, false);

    boolean b;

    b = Reachability.isReachable(0, 0, 3, 1, grid);
    System.out.println(b);

    b = Reachability.isReachable(3, 1, 0, 0, grid);
    System.out.println(b);
  }

  @Test
  void getClosestWalkablePointToTarget1() {
    Grid grid = new Grid(gridWidth, gridHeight);
    grid.setWalkable(2, 2, false);

    long p;

    p = Reachability.getClosestWalkablePointToTarget(0, 0, 2, 2, grid);
    System.out.println(Point.getX(p) + "-" + Point.getY(p));

    p = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 3, grid);
    System.out.println(Point.getX(p) + "-" + Point.getY(p));
  }

  @Test
  void getClosestWalkablePointToTarget2() {
    Grid grid = new Grid(gridWidth, gridHeight);
    grid.setWalkable(2, 2, false);

    long p;

    p = Reachability.getClosestWalkablePointToTarget(8, 8, 29, 21, 10, grid);
    System.out.println(Point.getX(p) + "-" + Point.getY(p));

    p = Reachability.getClosestWalkablePointToTarget(8, 8, 50, 50, 10, grid);
    System.out.println(Point.getX(p) + "-" + Point.getY(p));

    p = Reachability.getClosestWalkablePointToTarget(0, 4, 5, 4, 2, grid);
    System.out.println(Point.getX(p) + "-" + Point.getY(p));
  }
}
