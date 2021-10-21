package labyrinthdeVirus;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Player {
	String gender;
	int x,y;
	Integer immunity;
	Image img;
		
	Player(String gendr,int x,int y){
		this.x=x;
		this.y=y;
		immunity=5;
	    gender=gendr;
	}
	public Image playerImage() {
		switch(gender) {
		case "male": 
			{	 
				ImageIcon icon= new ImageIcon("male.png");
				img=icon.getImage();
				return(img);}
			case "female": 
			{
			ImageIcon icon= new ImageIcon("female.png");
			img=icon.getImage();
			return(img);}
			case "child": 
			{
			ImageIcon icon= new ImageIcon("child.png");
			img=icon.getImage();
			return(img);}
		
		}
		return null;

	}
	
}
