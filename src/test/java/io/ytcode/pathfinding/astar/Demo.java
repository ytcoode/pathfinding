package io.ytcode.pathfinding.astar;

public class Demo {

  public static void main(String[] args) {

    // Build a grid map of width 20 * 20
    Grid grid = new Grid(20, 20);

    // By default, all points in the grid map are walkable
    grid.setWalkable(5, 5, false);

    // Get a AStar object
    AStar aStar = new AStar();
    // AStar aStar = ThreadLocalAStar.current();

    // Search
    Path path = aStar.search(0, 0, grid.getWidth() - 1, grid.getHeight() - 1, grid);

    for (int i = 0; i < path.size(); i++) {
      long p = path.get(i);
      int x = Point.getX(p);
      int y = Point.getY(p);
      System.out.println(x + "-" + y);
    }
  }
}
