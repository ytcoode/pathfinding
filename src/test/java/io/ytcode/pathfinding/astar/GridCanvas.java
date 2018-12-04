package io.ytcode.pathfinding.astar;

import javax.swing.*;
import java.awt.*;

public abstract class GridCanvas extends JFrame {

  private static final int width = 800;
  private static final int height = 800;

  private static final int startX = 60;
  private static final int startY = 80;
  private static final int endX = startX + width;
  private static final int endY = startY + height;

  private static final int gridSize = 50;
  static final int gridWidth = width / gridSize;
  static final int gridHeight = height / gridSize;

  GridCanvas(String name) {
    super(name);
    setSize(endX, endY);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    // 画竖线，分x轴
    g.setColor(Color.BLACK);
    for (int i = 0; i <= gridWidth; i++) {
      int x = i * gridSize + startX;
      g.drawLine(x, startY, x, endY);
      if (i < gridWidth) {
        x = x + gridSize / 2 - 7;
        g.drawString("" + i, x, startY - 5);
        g.drawString("" + i, x, endY + 15);
      }
    }

    // 画横线，分y轴
    for (int i = 0; i <= gridHeight; i++) {
      int y = i * gridSize + startY;
      g.drawLine(startX, y, endX, y);
      if (i < gridHeight) {
        y = y + gridSize / 2 + 5;
        g.drawString("" + i, startX - 20, y);
        g.drawString("" + i, endX + 10, y);
      }
    }
  }

  protected void fillGrid(Graphics g, int x, int y, Color c) {
    x *= gridSize;
    y *= gridSize;

    x += startX;
    y += startY;

    g.setColor(c);
    g.fillRect(x, y, gridSize, gridSize);
  }

  protected void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color c) {
    x1 *= gridSize;
    y1 *= gridSize;

    x1 += startX;
    y1 += startY;

    x1 += gridSize / 2;
    y1 += gridSize / 2;

    x2 *= gridSize;
    y2 *= gridSize;

    x2 += startX;
    y2 += startY;

    x2 += gridSize / 2;
    y2 += gridSize / 2;

    g.setColor(c);
    g.drawLine(x1, y1, x2, y2);
  }
}
