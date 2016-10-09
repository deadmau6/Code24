package MAIN;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class runner {

	public static void main(String[] args) throws Exception
	{
		Matroschka mat = new Matroschka();
		
		String data = "penis lord";
		String key = "ketamine";
		
		Scanner scan = new Scanner(new File("fuckit.txt"));
		String all = "";
		while(scan.hasNext())
		{
			all+=scan.nextLine();
		}
		
		
		System.out.println(data + " " + key);
		
		byte[] penis = mat.encrypt(all, key);
		
		for(byte b: penis)
		{
			System.out.println(b);
			if(b == 0)
			{
				System.out.println("zero exists");
			}
		}
		
		
		//String massage = mat.decrypt(penis, key);
		
		//System.out.println("unencrypted:" + massage);
		
		try{
			System.out.println("buffer image");
			BufferedImage image = ImageIO.read(new File("celty.jpg"));
			System.out.println("hidemessage");
			mat.hideMessage(image, penis);
			ImageIO.write(image, "png", new File("celty.jpg"));
			
			
			image = ImageIO.read(new File("celty.jpg"));
			mat.getMessageFromImage(image);
			
		}catch(Exception e){e.printStackTrace();}
		
		
	}
}
