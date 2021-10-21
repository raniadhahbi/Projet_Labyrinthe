package labyrinthdeVirus;



public class Cell {
	byte[] walls= {1,1,1,1}; 
	Integer cx;
	Integer cy;
	int value;
   

	public Cell(int j, int i) {
		cx=i;
		cy=j;
		value=0;}

	public boolean checkwalls() {
	if(walls[0]==1 && walls[1]==1 && walls[2]==1 && walls[3]==1)
	return true;
	else return false;
	}
	
	
	
	

}
