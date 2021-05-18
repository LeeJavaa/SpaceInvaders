import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpaceShip extends Sprite{
  
  public SpaceShip(int x, int y){
    super(x,y);      //used to refer parent class constructors (parentclass/superclass)
    this.loadImage("res\\Ship.png");
    this.getImageDimensions();
    
  }
  
  public void move(){
    x += dx;
  }
  
  public void mouseEntered(MouseEvent me) {
        
  }
  public void mouseExited(MouseEvent me) {
   
  }
  
  
  
  
  
  
  
  
  
  public void keyPressed(KeyEvent e){
    int key = e.getKeyCode();
    
    if ((key == KeyEvent.VK_LEFT)  && (0 < this.getX())){ //checks if key pressed and if ship still in bounds
      dx = -12;
      move();
    }
    
    if ((key == KeyEvent.VK_RIGHT) && (this.getX() < 1800)){
      dx = 12;
      move();
    }
   
      
      
  }  
  
  public void keyReleased(KeyEvent e){
    int key = e.getKeyCode();
    
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
  }
}