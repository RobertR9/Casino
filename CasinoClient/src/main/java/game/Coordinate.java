package game;


public class Coordinate {
	public int row;
	public int col;
	public Coordinate(int row, int col){
		this.row = row;
		this.col = col;
	}
	@Override
	public int hashCode(){
		return ((Integer)row).hashCode() + ((Integer)col).hashCode();
	}
	@Override
	public boolean equals(Object o){
		return	(o instanceof Coordinate) &&
				(this.row == ((Coordinate)o).row) &&
				(this.col == ((Coordinate)o).col);
	}
}