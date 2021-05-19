import javax.sound.sampled.*; // Music
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File; // Music files
import java.io.IOException;
import java.util.List;
import java.util.ArrayList; //For listing 



public class Display extends JPanel implements ActionListener{  
  
  private SpaceShip spaceship = new SpaceShip(945,950);
  private Background retro = new Background(0,0);
  private Menu menuOverlay = new Menu(0, 0);
  private Score scoreOverlay = new Score(0, -10);
  private Start startOverlay = new Start(0, 0);
  private Level levelOverlay = new Level(0, 0);
  private Restart restartOverlay = new Restart(0, 0);
  private GameOver gameoverOverlay = new GameOver(0, 0);
  private List<Alien> alienList = new ArrayList<>();      //List is way of creating almost like an array of objects but with infinte amount
  private List<Shot> bulletList = new ArrayList<>();      //Using list for bullets, since there should be unlimited
  // List of ships to represent lives
  private List<SpaceShip> livesList = new ArrayList<>();  
  private int direction = 1;                              //direction the alien list is moving. can either be 1 for right or -1 for left
  private boolean paused = false;                         //variable determines if frames of the game is momentarily paused
  private int bulletDelay = 0;
  private Timer timer;
  // private int lastpos = 5;                                         //alien in last coloumn
  // private int lengthrow = 5; 
  private int lastpos = 7;                                         //alien in last coloumn
  private int lengthrow = 7; 
  private boolean gameover = false;
  private boolean menu = true;
  private int countPoints = 0;
  private int highScore = 0;
  private int pointValue = 50;
  private int level = 1;
  private int lives = 3;

  private Color myBlue = new Color(183, 227, 254);
                                     
  
  
  public Display(){                                       //constructor runs once
  
    addKeyListener(new TAdapter());  
    setFocusable(true);                                   //tried without, doesnt work. something to do with jpanelt
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(1920,1080));     
    createAliens();
    createLives();
    run();
  }
  
  private void run(){
    timer = new Timer(1, this);                   //timer in the class, will run actionPerformed
    timer.start();
  }
  
  
  @Override
  public void actionPerformed(ActionEvent e){

    if (menu == true) {
      repaint();
    }
    else {
    if (alienList.size() == 0)
      return;
    moveAliens();
    moveBullets();
    updateAliens();
    updateBullet();
    if (gameover != true){
      checkCollision();
      checkCollisionShip();
    }
    if (bulletDelay != 0) { //using the counter to slow down the shooting
        bulletDelay++;
      }
    repaint();
    }
  }

  private void restart(){
    if(gameover == true){
      lives = 3;
      alienList.clear();
      createAliens();
      createLives();
      countPoints = 0;
      gameover = false;
      lastpos = 7;
      pointValue = 50;
      level = 1;
    }
  }

  private void levelUp(){
    createAliens();
    lastpos = 7;    
    pointValue += 25;
    for (int i = 0; i < alienList.size(); i++){
      alienList.get(i).movementSpeed += 15;
    }
    level++;
  }

  private void restartLevel(){
    alienList.clear();
    createAliens();
    lastpos = 7;
    lives--;
    removeLife();
  }
  
  private void createAliens(){
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 8; j++) {
        Alien alien = new Alien(120*j , 70*i);  //Creating aliens and positioning them accordingly
        alienList.add(alien);
      }
    }
  } 

  private void createLives(){
    for (int i = 0; i < lives; i++) {
      SpaceShip temp = new SpaceShip(80*i , 10);  //Creating aliens and positioning them accordingly
      livesList.add(temp);
    }
  }
  
  private void removeLife(){
    livesList.remove(lives);
  }
  
   //movement of my objects.   when key pressed this runs
  private class TAdapter extends KeyAdapter { //when key pressed this runs
    
    @Override
    public void keyPressed(KeyEvent e){
      int key = e.getKeyCode();
      spaceship.keyPressed(e);   //movement of spaceship

      if (menu == true) {
        if (key == KeyEvent.VK_SPACE) {
          try {
            coinSound();
          } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
            e1.printStackTrace();
          }
          menu = false;
        }
        if (key == KeyEvent.VK_Q) {
          System.exit(0);
        }
      } else if (gameover == true) {
          if (key == KeyEvent.VK_Q) {
            System.exit(0);
          }
          if (key == KeyEvent.VK_SPACE){
            try {
              coinSound();
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
              e1.printStackTrace();
            }
            restart();
          }
      } else {
      // if ((key == KeyEvent.VK_SPACE) && (bulletDelay % 3 == 0)) { //using the counter to slow down the shooting
      //   //create a new bullet
      //   bulletDelay = 1;  
      //   Shot bullet = new Shot(spaceship.getX() + 21, spaceship.getY() - 20);//bullet must position ietself at the spacship
      //   bulletList.add(bullet);
      // }

        if ((key == KeyEvent.VK_SPACE)) { //using the counter to slow down the shooting
          //create a new bullet 
          Shot bullet = new Shot(spaceship.getX() + 21, spaceship.getY() - 20);//bullet must position ietself at the spacship
          try {
            laserSound();
          } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
            e1.printStackTrace();
          }
          bulletList.add(bullet);
        }
        if (key == KeyEvent.VK_Q) {
          System.exit(0);
        }
      }
 
    } 
    
   @Override
   public void keyReleased(KeyEvent e){
     spaceship.keyReleased(e);
   }
  }
  

  
  
  //paint background
 @Override 
 public void paintComponent(Graphics g){   //property of jpanel that automatically runs //repaint() does this

  super.paintComponent(g);
  if (menu==true){
    paintBackground(g);
  }
  else {
    paintBackground(g);
    paintSpaceShip(g);
    paintLaser(g);
    paintAliens(g);
    paintLives(g);
    
     if (gameover == true){
       gameOverSeq(g);
       return;
     }
  }
   
 }
 
 public void paintBackground(Graphics g){

  if (menu == true) {
    g.drawImage(retro.getImage(),retro.getX(), retro.getY(), this);  //draw background
    g.drawImage(menuOverlay.getImage(), menuOverlay.getX(), menuOverlay.getY(), this);
    g.drawImage(startOverlay.getImage(), startOverlay.getX(), startOverlay.getY(), this);
  }
  else {
    g.drawImage(retro.getImage(),retro.getX(), retro.getY(), this);  //draw background

    //scoreboard
    g.drawImage(scoreOverlay.getImage(),scoreOverlay.getX(), scoreOverlay.getY(), this);
    // String message = "SCORE: " + countPoints;
    String message = Integer.toString(countPoints);
    // Font font = new Font("Bauhaus 93", Font.BOLD, 64);
    Font font = new Font("Helvetica Neue", Font.BOLD, 64);
    // g.setColor(Color.RED);
    g.setColor(myBlue);
    g.setFont(font);
    // g.drawString(message , 50, 600);   //zetcode: https://zetcode.com/javagames/spaceinvaders/
    g.drawString(message, 250, 1042);

    // level
    g.drawImage(levelOverlay.getImage(),levelOverlay.getX(), levelOverlay.getY(), this);
    // Text for level
    String levelText = Integer.toString(level);
    g.drawString(levelText, 1750, 1042);
  }
                                         
}
 
 public void paintSpaceShip(Graphics g){
   g.drawImage(spaceship.getImage(), spaceship.getX(), spaceship.getY(), this);   //wants image then an x and y value then width and height  
 }
 
 public void paintLaser(Graphics g){
   for (Shot bullet : bulletList){ //print the list of bullets
      g.drawImage(bullet.getImage(), bullet.getX(), bullet.getY(), this); 
   }
 }
 
  public void paintAliens(Graphics g){
    for (Alien alien : alienList){ //print the list of aliens
      g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this); 
    }
  }

  public void paintLives(Graphics g){
    for (SpaceShip life : livesList){ //print the list of lives
      g.drawImage(life.getImage(), life.getX(), life.getY(), this); 
    }
  }
  

 
  
  
  //testing whether bullet hits alien..in that case alien must die
  //loop through aliens and bullets. (need to use width and height of object to check the bounds, then seeing if 
  //the bounds of the two objects intersects
  public void checkCollision(){
    //checks if bullet hits alien
    for (int i = 0; i < alienList.size(); i++){
      Alien temp = alienList.get(i); 
      for (Shot bullet : bulletList){
        
        if ((  bullet.getX() > (temp.getX() + 20)  ) &&
           (   bullet.getX() < (temp.getWidth() + temp.getX()) - 20 ) &&
           (   bullet.getY() >  temp.getY() )  &&
           (   bullet.getY() < (temp.getHeight() + temp.getY())  )    ){   //x and y is top left point
          temp.setVisible(false);
          bullet.setVisible(false);
          // alienList.remove(temp);
          // bulletList.remove(bullet);
          try {
            explosionSound();
          } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
          }
          countPoints += pointValue;
        }
      }
    }  
    
   //checks if alien crosses line and if game should be over
   outerloop:
    for (int i = 0; i < alienList.size(); i++){
      Alien temp = alienList.get(i);
      if ((temp.getY() >= 950) && (lives ==0)){
        gameover = true;
        try {
          gameoverSound();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
          e.printStackTrace();
        }
        break outerloop;
      }
      else if (temp.getY() >= 950){
        restartLevel();
        break outerloop;
      }     
    }

    // Checks if bullet leaves the screen and sets invisible. This will allow it to be deleted with update().
    for (int i = 0; i < bulletList.size(); i++){
      Shot temp = bulletList.get(i);
      if (temp.getY() < 0 ) {
        temp.setVisible(false);
      }
    } 

  }

  public void checkCollisionShip(){
    //checks if alien hits ship
    outerloop:
    for (int i = 0; i < alienList.size(); i++){
      Alien temp = alienList.get(i); 
      if (temp.getX() > (spaceship.getX()-20) &&
      (temp.getX() < (spaceship.getWidth() + spaceship.getX() + 20)) &&
      (temp.getY() > (spaceship.getY() - 25)) && (lives==0)){   //x and y is top left point
        gameover = true;
        try {
          gameoverSound();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
          e.printStackTrace();
        }
        break outerloop;
      } else if (temp.getX() > (spaceship.getX()-20) &&
      (temp.getX() < (spaceship.getWidth() + spaceship.getX() + 20)) &&
      (temp.getY() > (spaceship.getY() - 25))) {
        restartLevel();
        break outerloop;
      }
    }
  }
  
  
  public void moveAliens(){
    if (lastpos == 0){
      return;
    }
    
    for(int i = 0; i < alienList.size(); i++){
      Alien temp = alienList.get(i);
      temp.moveX(direction);                               //move alien right or left
    }
    
    if (alienList.get(lastpos).getX() >= 1800){                   //if alien at end of screen move down
      direction = -1;
      for (Alien alien : alienList)
        alien.moveY();
    }
    
    if (alienList.get(0).getX() <= 0){                           //if alien at end of screen move down
      direction = 1;
      for (Alien alien : alienList)
      alien.moveY();
    }    
  }
  
  public void moveBullets(){
    for (Shot bullet : bulletList){
      bullet.move();
    }
  }
 
  
  
  //updates position of aliens
  //remove objects from list if they are not visible
  public void updateAliens(){ 

   
    for (int j = 0; j <= lengthrow; j++){
      Alien temp2 = alienList.get(j);
      if (temp2.getVisible() == false){
        lastpos = lastpos - 1;
        System.out.print(lastpos);
        lengthrow--;
      }
    }
    
    
    
    for (int i = 0; i < alienList.size(); i++){
      Alien temp = alienList.get(i);
      
      if (temp.getVisible() == false) {    //if alien shot remove
        alienList.remove(temp);
      } 
    }

    if (alienList.size() == 0){
      levelUp();
    }
      
  }
  
  public void updateBullet(){
    for (int i = 0; i < bulletList.size(); i++){
      Shot temp = bulletList.get(i);
  
      if (temp.getVisible() == false) {
        bulletList.remove(temp);
      }
    } 
  }
  
  
  
  public void gameOverSeq(Graphics g){
    scoreCheck();
    g.drawImage(retro.getImage(),retro.getX(), retro.getY(), this);  //draw background
    g.drawImage(gameoverOverlay.getImage(), gameoverOverlay.getX(), gameoverOverlay.getY(), this);
    g.drawImage(restartOverlay.getImage(), restartOverlay.getX(), restartOverlay.getY(), this);
    // System.out.print("GAME OVER");
    String scoreMessage = Integer.toString(countPoints);
    String highScoreMessage = Integer.toString(highScore);

    Font font = new Font("Helvetica Neue", Font.BOLD, 64);
    g.setColor(myBlue);
    g.setFont(font);
    g.drawString(scoreMessage, 960, 825);
    g.drawString(highScoreMessage, 960, 925);
  }

  public void scoreCheck(){
    if (countPoints >= highScore){
      highScore = countPoints;
    }
  }

  // Laser sound effect

  public static void laserSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
    File laserFile = new File("newres\\audio\\Laser.wav");
      
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(laserFile);
    Clip laserClip = AudioSystem.getClip();
    laserClip.open(audioStream);

    laserClip.start();

    // Volume control: 

    // Get the gain control from clip
    FloatControl laserGainControl = (FloatControl) laserClip.getControl(FloatControl.Type.MASTER_GAIN);

    // set the gain (between 0.0 and 1.0)
    double gain = 0.5;   
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    laserGainControl.setValue(dB);
  }  

  // Explosion sound effect
  public static void explosionSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
    File explosionFile = new File("newres\\audio\\Explosion-deeper-edited.wav");
      
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(explosionFile);
    Clip explosionClip = AudioSystem.getClip();
    explosionClip.open(audioStream);

    explosionClip.start();

    // Volume control: 

    // Get the gain control from clip
    FloatControl explosionGainControl = (FloatControl) explosionClip.getControl(FloatControl.Type.MASTER_GAIN);

    // set the gain (between 0.0 and 1.0)
    double gain = 0.6;   
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    explosionGainControl.setValue(dB);
  }  

  public static void coinSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
    File coinFile = new File("newres\\audio\\Coin.wav");
      
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(coinFile);
    Clip coinClip = AudioSystem.getClip();
    coinClip.open(audioStream);

    coinClip.start();

    // Volume control: 

    // Get the gain control from clip
    FloatControl coinGainControl = (FloatControl) coinClip.getControl(FloatControl.Type.MASTER_GAIN);

    // set the gain (between 0.0 and 1.0)
    double gain = 1;   
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    coinGainControl.setValue(dB);
  }  

  public static void gameoverSound() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
    File gameoverFile = new File("newres\\audio\\Game-Over.wav");
      
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(gameoverFile);
    Clip gameoverClip = AudioSystem.getClip();
    gameoverClip.open(audioStream);

    gameoverClip.start();

    // Volume control: 

    // Get the gain control from clip
    FloatControl gameoverGainControl = (FloatControl) gameoverClip.getControl(FloatControl.Type.MASTER_GAIN);

    // set the gain (between 0.0 and 1.0)
    double gain = 1;   
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    gameoverGainControl.setValue(dB);
  }  
}
  
  
  
  
//Notes: 
/* I initially tried to use this timer that would run the game cycle.
initially it did work. However moving the spaceship with the keys gave me problems. The movement of the spaceship would have
to be on the same inteval as everything else. Which I didnt want...

import java.util.Timer;
import java.util.TimerTask; //timer
import java.util.concurrent.TimeUnit;  //used to delay 

    //timer...for game cycle 
    private Timer timer = new Timer();         //timer repaints() everything at every cycle
    private TimerTask task = new TimerTask(){  
      @Override                                //run method needs to be override                    
      public void run(){
        moveAliens();
        updateAliens();
        updateBullet();
        checkCollision();                             //check if bullets have hit
        repaint();
      }
    }; 
  
  timer.scheduleAtFixedRate(task,0,300); //repeats task every one second (which is the game cycle)  
  
  */
  
  