package labyrinthdeVirus;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;



public class MapCell {
	int n,m;
    Cell[][] cells;
    int x,y;
    Scanner scan;
    Image img;
    Point [] Virus;
  public MapCell(int width, int length)  {
  	n=width;m=length;
      cells = new Cell[n][m];
       
   
  }
  
  public void initCellMap() throws FileNotFoundException{
      for(int j=0; j<n; j++) { //line
          for(int i=0; i<m; i++) {   //col
              cells[j][i] = new Cell(j,i);
              }}
      
			addVirus();
	      	addCovid();
	      	addGel();
	      	addEnergie();
		
      
        
  }
  
  public void addVirus() throws FileNotFoundException{
	   scan = new Scanner(new File("virus.txt"));
  			while (scan.hasNext())
  			{
  			     y= scan.nextInt(); //line        
  			    x= scan.nextInt(); //colum
  				    if(y<n && y>=0 && x<m && x>=0 )
  				    	cells[y][x].value=1;
  				   
  			    }
  			
  			scan.close();
  	}
  
  public void addCovid() throws FileNotFoundException{
  	scan = new Scanner(new File("covid.txt"));
  	
  			while (scan.hasNext())
  			{
  				y= scan.nextInt(); //line        
  			    x= scan.nextInt(); //colum
  				    if(y<n && y>=0 && x<m && x>=0 ) {cells[y][x].value=2;}

  			    }
  			scan.close();
  	}
  
  public void addGel() throws FileNotFoundException{
	  scan = new Scanner(new File("gel.txt"));
  	
  			while (scan.hasNext())
  			{
  				y= scan.nextInt(); //line        
  			    x= scan.nextInt(); 
  				    if(y<n && y>=0 && x<m && x>=0 )
  				    	cells[y][x].value=3;
  			    }
  			scan.close();
  	}
  public void addEnergie() throws FileNotFoundException{
	  scan = new Scanner(new File("energie.txt"));
  	
  			while (scan.hasNext())
  			{
  				y= scan.nextInt(); //line        
  			    x= scan.nextInt(); 
  				    if(y<n && y>=0 && x<m && x>=0 )
  				    	cells[y][x].value=4;
  			    }
  			scan.close();
  	}


  public String element(int i,int j){   // console
  	int e= cells[i][j].value;
  	switch (e ){
  		case 0: {return(" ");}
  		case 1: {return("v");}
  		case 2: {return("c");}
  		case 3: {return("g");}
  		case 4: {return("e");}
  		
  		}
  	return null;

  		}
  

  public Image mapImage(int y, int x){ //line,col
	  	int e=cells[y][x].value;
	  	switch (e){
	  		case 1: 
	  		{	 
	  			ImageIcon icon= new ImageIcon("virus.png");
	  			img=icon.getImage();
	  			return img;}
	  		case 2: 
	  		{
	  		ImageIcon icon= new ImageIcon("covid.png");
				img=icon.getImage();
				return img;}
	  		case 3: 
	  		{
	  		ImageIcon icon= new ImageIcon("gell.png");
				img=icon.getImage();
				return img;}
	  		case 4: 	    	
	  		{
	  		ImageIcon icon= new ImageIcon("energy.png");
				img=icon.getImage();
				return img;}
	  		
	  		}
	  	return null;
	  		}
  
  
  
  
  
  
  
  
  
  
  
 
}





