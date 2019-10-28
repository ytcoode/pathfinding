package io.ytcode.pathfinding.astar;

public class TooLongPathException extends RuntimeException {

  TooLongPathException(String errMsg) {
    super(errMsg);
  }
}
