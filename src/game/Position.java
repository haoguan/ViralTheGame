package game;

public class Position {
	
	double positionAngle;
	double positionRadius;
	boolean positionUsed = false;
	Player player = null; //should only be one or the other could be set.
	Cell cell = null; //should only be one or the other could be set.
	
	
	public void setAngle(double angle){
		positionAngle = angle;
	}
	public void setPositionRadius(double radius) {
		positionRadius = radius;
	}
	public void setPositionUsed(boolean positionUsed){
		this.positionUsed = positionUsed;
	}
	public double getAngle(){
		return positionAngle;
	}
	public double getPositionRadius(){
		return positionRadius;
	}
	public boolean getPositionUsed(){
		return positionUsed;
	}
	public void setPlayer(Player player){
		this.player = player;
	}
	public Player getPlayer(){
		return player;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
	}
	public Cell getCell() {
		return cell;
	}
}
