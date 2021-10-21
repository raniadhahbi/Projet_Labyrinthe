package labyrinthdeVirus;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class LabyGame extends JFrame implements ActionListener , KeyListener {
	
    int cellsize =40;
    int offsetX=100;
    int offsetY=50;
    int pointX, pointY,oldX,oldY,nexty,nextx;
	Labyrinth laby ;
	boolean get_covid,get_gel;
	Integer chrono,Chrono;
	Timer clock,change_clock;
	JPanel  gamePanel,scorePanel;
	JLabel timer, immun;
	TextField timerText, immunText ;
	Player player;
	JPanel Infos;
	JButton About,winners;
	JTextField xbegin ,ybegin ,xend ,yend,width,height; 
    JPanel coordinate,startcoord , endcoord,dimension,gender;
    String genderText;
	ButtonGroup group ;
	int result;
	int w,h,xb,yb,xe,ye;
	boolean restart,fileErr;
	JPanel p,buttons;
	JTextArea txt;
	ImageIcon img;
	Object[] options = {"ok", "Exit"};
	 Scanner scan;
	 
//**************************LabyGame********************************************
	 
	public LabyGame() throws  IOException {
		setTitle("Labyrinth de virus");
		setVisible(true);
		requestFocusInWindow();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		restart=false;	
		 //create the score panel contains the clock and immunity value
		scorePanel = new JPanel();
		
			timer = new JLabel(new ImageIcon("clock.png"));
			timerText = new TextField("0", 1);
			timerText.setEditable(false);
			
			immun = new JLabel(new ImageIcon("immun.png"));
			immunText = new TextField("5", 1);
			immunText.setEditable(false);
			
			scorePanel.add(immun);
			scorePanel.add(immunText);
			scorePanel.add(Box.createHorizontalStrut(45)); // a spacer
			scorePanel.add(timer);
			scorePanel.add(timerText);
			scorePanel.add(Box.createHorizontalStrut(45));
			scorePanel.setBackground(Color.white);
			
		//create the game panel contains the labyrinth board
		gamePanel = new JPanel() {

			public void paint(Graphics g) {  
			//super.paint(g);
			try {
				DrawLabyrinth(g);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}};
		
		startGame();
				
				
	}
	
//**************************Main************************************
	
	public static void main(String[] args)   {
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LabyGame frame = new LabyGame();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);

				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}  catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

			
	}
	
//***********************Start Game***********************************
	
    public void startGame() throws IOException{

    	if(restart) {	
	    	 remove(scorePanel);
			 remove(gamePanel);}
    	fileErr=false;
    	chrono=0;
    	Chrono=0;
         //input game characteristics throw panel
    	inputData();
    	//input element coordinate throw files
        add("virus","virus.txt","virus_small.png");
    	add("covid","covid.txt","covid_small.png");
    	add("gel","gel.txt","gel_small.png");
    	add("energy","energie.txt","energy_small.png");
	
	
			 setSize (w*cellsize+200,h*cellsize+200); //size of the frame
               
	           //create the Labyrinth board and the player
				laby =new Labyrinth (w,h,xb,yb,xe,ye);
				laby.generateLaby();
				//verify the start and end cell are empty of element
				 if(laby.map.cells[yb][xb].value!=0 || laby.map.cells[ye][xe].value!=0) {error("your start or end cell is occupied ") ;}
				 
	            player=new Player (genderText,xb,yb);

	            //set the start cell in the frame
		        pointX=offsetX+xb*cellsize; 
				pointY=offsetY+yb*cellsize;
				oldX=pointX;
				oldY=pointY;
				//Initialize 
				get_covid =false;
				get_gel=false; 
				chrono=0;
				timerText.setText(chrono.toString());
				
				//add the score panel to the frame
				 setLayout(new BorderLayout());
				 add(scorePanel, BorderLayout.NORTH);
								
				//add the game panel to the frame
				  gamePanel.setBackground(Color.white);
				  add(gamePanel, BorderLayout.CENTER);
				
				
				//update the clock time
					clock=new Timer(1000,new ActionListener() {
						 //update the clock value
						@Override
						public void actionPerformed(ActionEvent e) {
							timerText.setText(chrono.toString());
						    chrono++;
							if (get_covid && chrono==61 && !get_gel) {	//player lose if he didn't get the gel in time 		
								gameLost("Time's up , you lost !");	}

				

			}	
		});
					//update the clock time
					change_clock=new Timer(1000,new ActionListener() {
						 //update the clock value
						@Override
						public void actionPerformed(ActionEvent e) {
						    Chrono++;
							
							if(Chrono % 50 ==0) randomChange();

				

			}	
		});
			
		

	}
    
//*****************************INPUT PANEL********************************************	
   
public void  inputData() throws IOException {
	//main data panel contains the dimensions , coordinate,gender player ,start and finish cell and description about the game ,winners names
	Infos= new JPanel();
	Infos.setLayout(new GridLayout (5,1));
	buttons =new JPanel();
	    About = new JButton("About");
	    About.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)  {
          
	    			JOptionPane.showMessageDialog(null,"Along the game, the player can be attack by several viruses .\r\n" + 
							 	"the immunity level reduce by one if he attacks by a normal virus. \r\n" + 
							 	"if the palyer attacked by a covid-19 virus, the immunity reduce by three and he must reach \r\n" + 
							 	"a cell containing a disinfectant gel within one minute otherwise he loses the game.\r\n" +
							 	"the immunity level regain by collecting energy potions. \r\n" + 
    							"the player win if he reach the finish cell with positive immunity,\r\n" + 
    							"and lose if the level of immunity is negative or zero\r\n" + 
    							"\r\n"
    							, "About ", JOptionPane.INFORMATION_MESSAGE); 
         }
     });
	    
	     winners = new JButton("Winners");	   
	     winners.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e1)  {
	        
	        	{
	        	    JPanel winNames=new JPanel(); 
	        	    JTextArea ta = new JTextArea(20,40);
	        	    ta.setEditable(false);
	        	    winNames.add(new JScrollPane(ta));
	        	    pack();
	        	    try{ta.read(new FileReader("WINNER.txt"),null);}catch(IOException ioe){}
	        	    JOptionPane.showMessageDialog(null,winNames, "Winners ", JOptionPane.INFORMATION_MESSAGE); 
	        	  }
	             
	     }
	     });
	    
	    buttons.add(About);
	    buttons.add(winners);
	     
	     
	     
	    dimension = new JPanel();
	      width = new JTextField(2);
	      height = new JTextField(2);
	      dimension.add(new JLabel("height  :" ));
	      dimension.add(height );
	      dimension.add(Box.createHorizontalStrut(25)); // a spacer
	      dimension.add(new JLabel("width   :" ));
	      dimension.add(width );
        
	    

       startcoord = new JPanel();
          xbegin = new JTextField(2);
	      ybegin = new JTextField(2);
          startcoord.add(new JLabel("Start:  y"));
          startcoord.add(ybegin);
          startcoord.add(Box.createHorizontalStrut(25)); // a spacer
          startcoord.add(new JLabel("   x    :"));
          startcoord.add(xbegin);
          
       endcoord = new JPanel();
          xend = new JTextField(2);
          yend = new JTextField(2);
          endcoord.add(new JLabel("End:    y"));
          endcoord.add(yend);
          endcoord.add(Box.createHorizontalStrut(25)); // a spacer
          endcoord.add(new JLabel("   x    :"));
          endcoord.add(xend);

          
     
    gender = new JPanel();
     gender.setLayout(new GridLayout(1,4));
      group = new ButtonGroup();
        JRadioButton option1 = new JRadioButton("male");
        JRadioButton option2 = new JRadioButton("female");
        JRadioButton option3 = new JRadioButton("child");
        	group.add(option1);
        	group.add(option2);
        	group.add(option3);
   
        	gender.add(new JLabel("gender :") );
        	gender.add(option1);
        	gender.add(option2);
        	gender.add(option3);
    
        Infos.add(buttons);
        Infos.add(dimension);
        Infos.add(gender);
        Infos.add(startcoord);
        Infos.add(endcoord);
		setLocationRelativeTo(null);

             //set the input data
        Object[] option = {"Play", "Exit"};
		int res = JOptionPane.showOptionDialog(this,Infos,"Please input the necessary Informations", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);
        if(res==0) {	
		//Convert the data 
          	  w=Integer.parseInt(width.getText());
          	  h = Integer.parseInt(height.getText());
          	  xb=Integer.parseInt(xbegin.getText());
          	  yb=Integer.parseInt(ybegin.getText());
          	  xe=Integer.parseInt(xend.getText());
          	  ye=Integer.parseInt(yend.getText());
  
        	  genderText = null ;
            if (option1.isSelected()) {genderText ="male";}
            else if (option2.isSelected()) {genderText ="female";}
            else if (option3.isSelected()) {genderText ="child";}
            
        	     //test input data errors
        		if (( w <0 ) || (h<0 )) {error("your dimension are invalid") ;}
        		else if (( xb <0 ) || (xb > w )) { error("your cell coordinate is out of range");}
        		else if (( yb <0 ) || (yb > h )) {error("your cell coordinate is out of range");}
        		else if (( xe <0 ) || (xe > w )) {error("your cell coordinate is out of range");}
        		else if (( ye <0 ) || (ye > h )) {error("your cell coordinate is out of range");}
        		else if (!option1.isSelected() && !option2.isSelected() && !option3.isSelected()) {error("your gender is missing ");}
        		else if (( xb==xe ) && (yb == ye )) {error("your start and end cell are the same position") ;}
        		
        		






        }
        else if(res==1) {System.exit(0);}

        	

}
//-----------read file and write new coordinate in it after verification---

@SuppressWarnings("resource")
	void add(String type,String cellfile, String fileimg) throws IOException {
		setLocationRelativeTo(null);
		p= new JPanel();
		txt = new JTextArea(10,10);
		 img=new ImageIcon(fileimg);		
	    p.add(new JScrollPane(txt));
	    pack();
	    try{txt.read(new FileReader(cellfile),null);}catch(IOException ioe){}
	    Object[] options = {"Add","Cancel"};
		int select = JOptionPane.showOptionDialog(this,p,"Add"+type+" position", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, img, options, options[0]);
   	if(select==0)
   		{if(textValid(txt)) {
   		
   		FileOutputStream file = new FileOutputStream(cellfile, false);
   		file.write(txt.getText().getBytes());}
   		
   		else { 	fileErr=true;
   		error(type+" is out of range!") ;
   		fileErr=false;
   		add(type,cellfile, fileimg);
   		
   		}
   		
   		}
       else if(select==1) {System.exit(0);}

	} 

//-------------verify if the textArea coordinate are valid----------------

Boolean textValid(JTextArea tx ) {
	   scan = new Scanner(tx.getText());
			while (scan.hasNext())
			{
			     int row= scan.nextInt(); //row        
			   int  col= scan.nextInt(); //col
				    if(row<0 || row>=h || col<0 || col>=w )
				    	 return(false);
				   
			    }
			scan.close(); return(true);} 

//----------------error dialog and request to reset the data-------------------
  void error(String str) throws IOException {
	int res1 = JOptionPane.showOptionDialog(this,str+" try again","Information Error", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    if(res1==0) {
    	if(!fileErr) inputData();
    	}
    else if(res1==1) {System.exit(0);}	
}
  


//******************************Graphics*************************************
  
 public void DrawLabyrinth(Graphics g) throws IOException {
	
	 g.setColor(Color.black);
	((Graphics2D) g).setStroke(new BasicStroke(4)); //bold of the wall
		
   ((Graphics2D) g).setBackground(Color.white);
   
        int x,y;
      for(Integer j=0;j<h;j++) {
    	  y=j*cellsize+offsetY;
    	  	for(Integer i=0;i<w;i++) {
    	  		x=i*cellsize+offsetX;
    	  	     //draw walls line by line column by column

    	  		if(laby.map.cells[j][i].walls[0]==1) { //up wall
    	  			g.drawLine(x, y, x+cellsize, y);}
    	  											

    	  		if(laby.map.cells[j][i].walls[1]==1) { // right wall
    	  			g.drawLine(x+cellsize, y, x+cellsize, y+cellsize);}
    	  		
    	  		if(laby.map.cells[j][i].walls[2]==1) { //Bottom wall
    	  			g.drawLine(x, y+cellsize, x+cellsize, y+cellsize);}
    	  		
    	  		if(laby.map.cells[j][i].walls[3]==1) { //left wall
    	  			g.drawLine(x, y, x, y+cellsize);}

	
	//draw each element by image
         if(i==laby.x0 && j==laby.y0) {
        	 Image img;
        	 ImageIcon icon= new ImageIcon("start.png");
        	 img=icon.getImage();
        	 g.drawImage(img , x,y ,cellsize,cellsize, null);} 
         
         
         else if(i==laby.xf && j==laby.yf) {
        	 Image img;
        	 ImageIcon icon= new ImageIcon("finish.png");
        	 img=icon.getImage();
        	 g.drawImage(img , x,y ,cellsize,cellsize, null);}
      
       
         else if(laby.map.cells[j][i].value!=0)	
        	 g.drawImage(laby.map.mapImage(j,i) , x+cellsize/4,y+cellsize/4 ,cellsize/2,cellsize/2, null);
	
	}
	
     }
      
//------------------------------------Movement-------------------------------------
 	 change_clock.start();
      
      
    //old frame coordinate
 x= (oldX-offsetX)/cellsize;  
 y= (oldY-offsetY)/cellsize;
     //next frame coordinate
 nextx=(pointX-offsetX)/cellsize; 
 nexty=(pointY-offsetY)/cellsize; 
 
    //set the current position of the player
 		player.x=x;
 		player.y=y;
 		
 		//disable the movement on left if there's a wall
 		if(x>=0 && x<laby.m && oldX > pointX && laby.map.cells[y][x].walls[3]==1)
 			{pointX=oldX;
 			pointY=oldY;
 			}
 		
 		//disable the movement on right if there's a wall
 		else if(x>=0 && x<laby.m && oldX < pointX && laby.map.cells[y][x].walls[1]==1)
			{pointX=oldX;
			pointY=oldY;
			}
 		
 		//disable the movement on down if there's a wall
 		else if(y>=0 && y<laby.n && oldY < pointY && laby.map.cells[y][x].walls[2]==1)
 			{pointX=oldX;
			pointY=oldY;
			}
 		
 		//disable the movement on top if there's a wall
 		else if(y>=0 && y<laby.n && oldY > pointY && laby.map.cells[y][x].walls[0]==1)
 			{pointX=oldX;
			pointY=oldY;
			}
 		
 		//draw the movement 
 		else if(pointX != oldX || pointY!=oldY ) {
			   immunText.setText(player.immunity.toString());
 	            checkGame(y,x);
 	       //check the rules of the game
		     g.drawImage(player.playerImage(),pointX+2,pointY+2,cellsize-4,cellsize-4, null);
 	 		  checkImmunity(nexty,nextx); 
 	 		  }
 		
        g.drawImage(player.playerImage(),pointX+2,pointY+2,cellsize-4,cellsize-4, null);


}
 
 //---------------------Immunity update-------------------

 	public void checkImmunity(int ny, int nx ) {
 		
 		//pass by a virus 
			if(laby.map.cells[ny][nx].value==1 ) {
				player.immunity=player.immunity-1;
				}
			
 		//hit by covid-19
			else if(laby.map.cells[ny][nx].value==2) {
 				player.immunity=player.immunity-3;
 				if(!get_covid) {
 				chrono=0;
 				clock.start();
 				get_covid=true; get_gel=false;}
 					
 			}
 			
 		 //reach a gel 
 			else if(laby.map.cells[ny][nx].value==3 ) {
 				get_gel=true;
 				player.immunity=5;
 			          if( get_covid && chrono<=60) {
 			        	  	chrono=0;
 			      		  timerText.setText(chrono.toString());
 			        	  	clock.stop();
 			        	  	get_covid=false;}
 			}
			//pass by an energy point
 			else if(laby.map.cells[ny][nx].value==4) {player.immunity=5;}
			
 			else player.immunity=player.immunity;
 			 
			   immunText.setText(player.immunity.toString());
 	}
 	
 	
 	//------------------lose or win the game verification----------------------------
 	public void checkGame (int oy , int ox) throws IOException {
 		//the win rule
 		if( ox==laby.xf && oy==laby.yf &&   player.immunity >0 ) {
 				clock.stop();
 				gameWon();		}

	 		if (laby.map.cells[oy][ox].value !=0 && player.immunity <=0){
			  		        immunText.setText(player.immunity.toString());
							clock.stop();
							gameLost("the immunity decreased, you lose!");	
	 			}
 	}
 	

			    
 //*************************Won the game dialog********************************			
 		
     private void gameWon() throws IOException  {
    	 clock.stop();
    		restart=true;
    		 int res1=JOptionPane.showConfirmDialog(null,"Congratulations YOU WON,put your name in the winner's list ?", "winner", JOptionPane.YES_NO_OPTION);
    		 if(res1 == 0) {
    	         String nom = JOptionPane.showInputDialog(null, "Name :", JOptionPane.INFORMATION_MESSAGE);
    	 		 FileWriter fw = new FileWriter("WINNER.txt",true);
    	 		 fw.write("\n"+ nom + ":     immunity = "+player.immunity);
    	 		 fw.close();
    		 }
    		Object[] options = {"Play again", "Exit"};
    		int res = JOptionPane.showOptionDialog(this,"Press yes if you want to restart ! ", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
    		      if(res == 0) {
    		    	
    		    	  try {  					  
								startGame();							
    				   } catch (FileNotFoundException e) {
							e.printStackTrace();
						}  catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    		    	
    	      } else if (res == 1) { System.exit(res);}   	
}

     //**********************lost the dialog************************************
     
   public void gameLost(Object ob) {
	   clock.stop();
		restart=true;
		Object[] options = {"Play again", "Exit"};
		int res = JOptionPane.showOptionDialog(this,ob+" press yes if you want to restart ! ","Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	   //int res=JOptionPane.showConfirmDialog(null, ob+" press yes if you want to restart ! ", "Game Over", JOptionPane.YES_NO_OPTION);
	   if(res == 0) {
		  
		   try {
				startGame();
				
		   }  catch (FileNotFoundException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
	
      } else if (res == 1) {
         System.exit(res);	
      } 
}


		
   //********************Change cell element randomly*******************
   void randomChange() {
		  Random rand = new Random();
			int rand_row=rand.nextInt(h) ;
	  		int rand_col=rand.nextInt(w) ;
		 
		 if(laby.map.cells[rand_row][rand_col].value !=0   ) {
			 int val =laby.map.cells[rand_row][rand_col].value;
	  			laby.map.cells[rand_row][rand_col].value =0;
	  			 rand_row=rand.nextInt(h) ;
		  		 rand_col=rand.nextInt(w) ;
	  		
		  		 if(laby.map.cells[rand_row][rand_col].value ==0 && (rand_row != yb && rand_col!=xb ) ||(rand_row != ye && rand_col!=xe ))
		  			 {laby.map.cells[rand_row][rand_col].value = val ;}
		  }
		 else {randomChange();}
			 
   
   }
   
   
   
   
//***************************action of the key arrows*************************************

@SuppressWarnings("static-access")
@Override
public void keyPressed(KeyEvent key) {
	oldX = pointX;
	oldY = pointY;
	
	if(key.getKeyCode()==key.VK_DOWN) 
	{pointY = pointY + cellsize;
	   if(pointY > getBounds().height) 
	   {pointY = getBounds().height;} 
	}
	if(key.getKeyCode()==key.VK_UP) 
	{pointY = pointY - cellsize;
	   if(pointY < 0) {pointY =0;}  
	}
	
	if(key.getKeyCode()==key.VK_LEFT) 
	{pointX = pointX - cellsize;
	   if(pointX < 0) {pointX = 0;} 
	}
	
	if(key.getKeyCode()==key.VK_RIGHT) 
	{pointX = pointX + cellsize;
	   if(pointX > getBounds().width) 
	   {pointX = getBounds().width;} 
	}
	repaint();
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}





}






	

