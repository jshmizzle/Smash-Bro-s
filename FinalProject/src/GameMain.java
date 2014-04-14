import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//RANDOM COMMMENT
public class GameMain {

	public static void main(String[] args) {
	//	Game g = new Game();
		BufferedImage image = null;
		
		
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		
		
		File imageFile1 = new File("MegaMan.gif");
		File imageFile2 = new File("Sonic.gif");
		
		try {
			image = ImageIO.read(imageFile1);
			images.add(image);
			image = ImageIO.read(imageFile2);
			images.add(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}