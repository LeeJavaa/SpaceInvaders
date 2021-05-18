import java.awt.event.KeyEvent;
public class Shot extends Sprite{
  
  public Shot(int x, int y){
    super(x,y);
    this.loadImage("res\\Laser.png");
    this.getImageDimensions();
  }
  
  public void move(){
    y += -10;
  }  
}