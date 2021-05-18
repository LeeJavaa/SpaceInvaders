import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.EventQueue;
import javax.sound.sampled.*; // Music
import java.io.File; // For music
import java.io.IOException;

public class Game extends JFrame{
  public Game(){
    userInt();
  }
                  
  public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
    Game ex = new Game();
    music();
    ex.setVisible(true);
  }
  
  
  private void userInt(){
    add(new Display());
    setResizable(false);
    pack();                          //All Jframe stuff
    setTitle("Space Invaders");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  } 

  public static void music() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
    File musicFile = new File("newres\\audio\\c152 - Night Mission.wav");
      
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
    Clip clip = AudioSystem.getClip();
    clip.open(audioStream);

    clip.loop(Clip.LOOP_CONTINUOUSLY);

    // Volume control: 

    // Get the gain control from clip
    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

    // set the gain (between 0.0 and 1.0)
    double gain = 0.5;   
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    gainControl.setValue(dB);
  }
}