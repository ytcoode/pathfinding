package io.ytcode.pathfinding.astar;

import java.util.Arrays;

import static io.ytcode.pathfinding.astar.Grid.MAX_OPEN_NODE_SIZE;
import static io.ytcode.pathfinding.astar.Node.getF;
import static io.ytcode.pathfinding.astar.Node.getX;
import static io.ytcode.pathfinding.astar.Node.getY;
import static io.ytcode.pathfinding.astar.Node.toNode;

class Nodes {
  Grid map;
  //  private Node[] nodes;
  private long[] nodes;
  private int size;

  Nodes() {
    //    this.nodes = new Node[16];
    this.nodes = new long[16];
  }

  void open(int x, int y, int g, int h, int pd) {
    if (size >= MAX_OPEN_NODE_SIZE) {
      throw new TooLongPathException("TooManyOpenNodes! max: " + MAX_OPEN_NODE_SIZE);
    }

    if (size >= nodes.length) {
      grow(size + 1);
    }

    long node = node(x, y, g, h, pd);
    siftUp(size, node);
    size++;
  }

  long close() {
    if (size == 0) {
      return 0; // f一定不为0，所以node肯定不为0
    }
    long r = nodes[0];
    size--;
    if (size > 0) {
      long n = nodes[size];
      //      nodes[size] = r;
      siftDown(0, n);
    }
    map.nodeClosed(getX(r), getY(r));
    return r;
  }

  long getOpenNode(int i) {
    assert i >= 0 && i < size;
    return nodes[i];
  }

  void openNodeParentChanged(long n, int idx, int pd) {
    //    assert nodes[idx] == n;
    siftUp(idx, n);
    map.nodeParentDirectionUpdate(getX(n), getY(n), pd);
  }

  void clear() {
    size = 0;
    map.clear();
    map = null;
  }

  boolean isClean() {
    return size == 0;
  }

  private static final int HEAP_SHIFT = 2;

  private void siftUp(int i, long n) {
    int nf = getF(n);
    while (i > 0) {
      int pi = (i - 1) >>> HEAP_SHIFT;
      long p = nodes[pi];
      if (nf >= getF(p)) {
        break;
      }
      setNode(i, p);
      i = pi;
    }
    setNode(i, n);
  }

  private void siftDown(int i, long n) {
    int nf = getF(n);
    while (i < size) {
      // 找children中最小的
      int ci = (i << HEAP_SHIFT) + 1;
      if (ci >= size) {
        break;
      }
      long c = nodes[ci];

      int cj = ci + 1;
      if (cj < size) {
        if (getF(nodes[cj]) < getF(c)) {
          c = nodes[ci = cj];
        }

        if (++cj < size) {
          if (getF(nodes[cj]) < getF(c)) {
            c = nodes[ci = cj];
          }

          if (++cj < size) {
            if (getF(nodes[cj]) < getF(c)) {
              c = nodes[ci = cj];
            }
          }
        }
      }

      // 和最小的child比较
      if (nf <= getF(c)) {
        break;
      }
      setNode(i, c);
      i = ci;
    }
    setNode(i, n);
  }

  private void setNode(int i, long n) {
    nodes[i] = n;
    map.openNodeIdxUpdate(getX(n), getY(n), i);
  }

  private long node(int x, int y, int g, int h, int pd) {
    //    long node = nodes[size];
    //    if (node == null) {
    //      node = new Node();
    //    }

    //    node.x = x;
    //    node.y = y;
    //
    //    node.g = g;
    //    node.f = g + h;

    long node = toNode(x, y, g, g + h);

    //    long node = setX(0, x);
    //    node = setY(node, y);
    //    node = setG(node, g);
    //    node = setF(node, g + h);

    map.nodeParentDirectionUpdate(x, y, pd);
    return node;
  }

  private void grow(int minCapacity) {
    int oldCapacity = nodes.length;
    int newCapacity = oldCapacity + ((oldCapacity < 64) ? (oldCapacity + 2) : (oldCapacity >> 1));

    if (newCapacity < minCapacity) {
      newCapacity = minCapacity;
    }

    if (newCapacity < 0) {
      throw new RuntimeException("Overflow");
    }
    nodes = Arrays.copyOf(nodes, newCapacity);
  }
}
