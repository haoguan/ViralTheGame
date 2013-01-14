package game;

import org.newdawn.slick.Image;

public class Cell {
	
	int cellColor; //one of the four player colors in Player.java
	Player player;
	Image cellImage = null;
	Tile currentTile;
	Position currentPosition;
	float[] cellCoords;
	
	public Cell(Player player, Tile curTile) {
		cellColor = player.getColor();
		this.player = player;
		currentTile = curTile;
		currentPosition = curTile.getCellPosition(this); //sets the position already automatically.
	}
	
	//Setter methods.
	public void setImage(Image image) {
		cellImage = image;
	}
	
	public void setCurrentTile(Tile tile) {
		currentTile = tile;
	}
	
	public void setCurrentPosition(Position pos) {
		currentPosition = pos;
	}
	
	public void setCellCoords(float[] coords) {
		cellCoords = coords;
	}
	
	//Getter methods.
	public Image getImage() {
		return cellImage;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Tile getCurrentTile() {
		return currentTile;
	}
	
	public Position getCurrentPosition() {
		return currentPosition;
	}
	
	public float[] getCellCoords() {
		return cellCoords;
	}
	
	
}
