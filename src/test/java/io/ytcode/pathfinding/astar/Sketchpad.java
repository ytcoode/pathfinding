package io.ytcode.pathfinding.astar;

import javax.swing.*;
import java.awt.*;

public abstract class Sketchpad extends JFrame {

  private static final int width = 1800; // 90
  private static final int height = 1000; // 50

  private static final int startX = 50;
  private static final int startY = 60;
  private static final int endX = startX + width;
  private static final int endY = startY + height;

  private static final int gridSize = 20;
  static final int gridWidth = width / gridSize;
  static final int gridHeight = height / gridSize;

  Sketchpad(String name) {
    super(name);
    setSize(width, height);
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
        g.drawString("" + i, x + 3, startY - 5);
        g.drawString("" + i, x + 3, endY + 15);
      }
    }

    // 画横线，分y轴
    for (int i = 0; i <= gridHeight; i++) {
      int y = i * gridSize + startY;
      g.drawLine(startX, y, endX, y);
      if (i < gridHeight) {
        g.drawString("" + i, startX - 18, y + 15);
        g.drawString("" + i, endX + 3, y + 15);
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
