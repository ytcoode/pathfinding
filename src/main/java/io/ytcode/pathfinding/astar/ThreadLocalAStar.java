package io.ytcode.pathfinding.astar;

public class ThreadLocalAStar {

  private static final ThreadLocal<AStar> local = ThreadLocal.withInitial(AStar::new);

  public static AStar current() {
    return local.get();
  }
}
