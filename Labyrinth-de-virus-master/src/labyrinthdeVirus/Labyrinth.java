package labyrinthdeVirus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public class Labyrinth {
	int n,m;
	int x0,y0;
	int xf,yf;
	MapCell map;
	public Labyrinth(int w, int h,int x0,int y0,int xf,int yf) throws FileNotFoundException  {
		   n=h;m=w;
           this.x0=x0;this.y0=y0;
           this.xf=xf;this.yf=yf;
		   map = new MapCell(n,m);
	       map.initCellMap();
	       //generateLaby();
				
	}
	

	public void display() { //console

		for (int i = 0; i < n; i++) {
			// draw the north edge
			for (int j = 0; j < m; j++) {
				System.out.print((map.cells[i][j].walls[0] ) == 1 ? "+---" : "+   ");
			}
	      
			System.out.println("+");
			// draw the west edge
			for (int j = 0; j < m; j++) {
	 System.out.print((map.cells[i][j].walls[3]) == 1 ? "| "+map.element(i,j)+" " : "  "+map.element(i,j)+" " );

			}
			System.out.println("|");
		}
		// draw the bottom line
		for (int j = 0; j < m; j++) {
			System.out.print("+---");
		}
		System.out.println("+");
	}


	public void generateLaby()
	{
	int x= x0; //first col
	int y= y0;//first line
	Stack <Cell> cellStack = new Stack<Cell>() ;
	int totalCells=n*m;
	int visitedCells=1;
	Cell currentCell = map.cells[y][x];
	ArrayList<Path> neighboreCellList = new ArrayList<Path>();
	Path tmpth=new Path();
	while(visitedCells<totalCells) {
		neighboreCellList.clear();
		tmpth=new Path();
		if(y-1>=0 && map.cells[y-1][x].checkwalls()==true) {
		tmpth.x1=x;
		tmpth.y1=y;
		tmpth.x2=x;
		tmpth.y2=y-1;
		tmpth.wall1=0;
		tmpth.wall2=2;
		neighboreCellList.add(tmpth);	
		}
		
		tmpth=new Path();
		if(y+1<n && map.cells[y+1][x].checkwalls()==true) {
		tmpth.x1=x;
		tmpth.y1=y;
		tmpth.x2=x;
		tmpth.y2=y+1;
		tmpth.wall1=2;
		tmpth.wall2=0;
		neighboreCellList.add(tmpth);	
		}
		tmpth=new Path();
		if(x-1>=0 && map.cells[y][x-1].checkwalls()==true) {
		tmpth.x1=x;
		tmpth.y1=y;
		tmpth.x2=x-1;
		tmpth.y2=y;
		tmpth.wall1=3;
		tmpth.wall2=1;
		neighboreCellList.add(tmpth);	
		}
		tmpth=new Path();
		if(x+1<m && map.cells[y][x+1].checkwalls()==true) {
		tmpth.x1=x;
		tmpth.y1=y;
		tmpth.x2=x+1;
		tmpth.y2=y;
		tmpth.wall1=1;
		tmpth.wall2=3;
		neighboreCellList.add(tmpth);	
		}
		
		
		  Random rand = new Random();
		  if(neighboreCellList.size()>=1) {
			int r1=rand.nextInt(neighboreCellList.size()) ;
			tmpth=neighboreCellList.get(r1);
			map.cells[tmpth.y1][tmpth.x1].walls[tmpth.wall1]=0;
			map.cells[tmpth.y2][tmpth.x2].walls[tmpth.wall2]=0;
			
			cellStack.push(currentCell);
			currentCell=map.cells[tmpth.y2][tmpth.x2];
			x=currentCell.cx;
			y=currentCell.cy;
			visitedCells++;
		 }
		 else {
			 currentCell= cellStack.pop();
			 x=currentCell.cx;
				y=currentCell.cy;
		 }
		 }
	
	}
	 

	
}
