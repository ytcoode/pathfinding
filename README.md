# A*

___A fast, GC-free Java implementation of A* (A Star) algorithm.___

## Usage

```java
// Build a grid map of 20 * 20
Grid grid = new Grid(20, 20);

// By default, all the points in the grid map are walkable
grid.setWalkable(5, 5, false);

// Get a AStar object
AStar aStar = new AStar();
// AStar aStar = ThreadLocalAStar.current();

// Search
Path path = aStar.search(0, 0, grid.getWidth() - 1, grid.getHeight() - 1, grid);

// Output
for (int i = 0; i < path.size(); i++) {
  long p = path.get(i);
  int x = Point.getX(p);
  int y = Point.getY(p);
  System.out.println(x + "-" + y);
}
```

## Benchmark

CPU: Intel(R) Core(TM) i7-7600U CPU @ 2.80GHz

```
From 0-0 to 99-99 -> 48567 ops/sec
From 0-0 to 99-99 -> 77580 ops/sec
From 0-0 to 99-99 -> 77459 ops/sec

From 0-0 to 199-199 -> 31486 ops/sec
From 0-0 to 199-199 -> 32321 ops/sec
From 0-0 to 199-199 -> 32041 ops/sec

From 0-0 to 299-299 -> 18440 ops/sec
From 0-0 to 299-299 -> 18409 ops/sec
From 0-0 to 299-299 -> 18245 ops/sec

From 0-0 to 399-399 -> 12355 ops/sec
From 0-0 to 399-399 -> 12355 ops/sec
From 0-0 to 399-399 -> 12424 ops/sec

From 0-0 to 499-499 -> 9447 ops/sec
From 0-0 to 499-499 -> 9317 ops/sec
From 0-0 to 499-499 -> 9360 ops/sec
```

## Screenshots

![Image of AStar](images/astar.png)