package io.ytcode.pathfinding.astar;

class TooLongPathException extends RuntimeException {

  TooLongPathException(String errMsg) {
    super(errMsg);
  }
}
