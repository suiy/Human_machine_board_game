/* Chip.java */

package player;

/**
 * A protected class for holding fields of a chip object.
 */
public class Chip {

  protected int color;
  protected int x;
  protected int y;
  protected boolean visited;

  // Chip constructor.
  protected Chip(int color, int x, int y) {
    this.color = color;
    this.x = x;
    this.y = y;
    visited = false;
  }

  // getter methods:

  // get color of Chip object
  protected int getColor() {
    return color;
  }

  // get x position of Chip object
  protected int getX() {
    return x;
  }

  // get y position of Chip object
  protected int getY() {
    return y;
  }

  // get visited state of Chip object
  protected boolean getVisited() {
    return visited;
  }

  // setter methods:

  // set color of Chip object
  protected void setColor(int color) {
    this.color = color;
  }

  // set x position of Chip object
  protected void setX(int x) {
    this.x = x;
  }

  // set y position of Chip object
  protected void setY(int y) {
    this.y = y;
  }

  // set visited state of Chip object
  protected void setVisited(boolean visited) {
    this.visited = visited;
  }

}
