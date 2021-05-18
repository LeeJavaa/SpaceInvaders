public class Alien extends Sprite{
  private int movementCounter = 0;
  
  
  public Alien(int x, int y){
    super(x,y);
    this.loadImage("res\\Alien.png");
    this.getImageDimensions();
  }
  public void moveX(int d){                  //move alien right or left 
    movementCounter++;
    if (movementCounter%50==0){              //only when count is 100. meaning that there is a dealy on the alien movement speed
      x += (d * (this.getWidth() - 40 ));   //otherwise the aliens and spaceship move at the same speed
                                            
    }
  }
  
   public void moveY(){
     if (movementCounter%50==0){
     y +=  (this.getHeight());
     }
   }
}