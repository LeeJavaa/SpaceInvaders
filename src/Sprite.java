import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle; //for comparing two obejct intersecting

public class Sprite{ //this is every object moving on the screen, even the backround(easy picture upload)
  protected int x;      //private can be accessed only by the functions inside the class.
  protected int y;
  protected int dx;
  protected int width;
  protected int height;
  protected boolean visible;
  protected Image image;
   
  public Sprite(int x, int y){
    this.x = x;
    this.y = y;   
    visible = true; //when object created
  }
  
  protected void loadImage(String imageName){
    ImageIcon pic = new ImageIcon(imageName);
    image = pic.getImage(); //GetImage of ImageIcon class
  }
  
  public Image getImage(){
    return image;
  }
  
  protected void getImageDimensions(){
    width = image.getWidth(null);       //zetcode image gets w and h
    height = image.getHeight(null); 
  }
  
  public int getX(){
    return x;
  }
  
  public int getY(){
    return y;
  }
  
 
  
  public boolean getVisible(){
    return visible;
  }
  
  public void setVisible(boolean visible){
    this.visible = visible;
  }
  
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }
  
  
    
}