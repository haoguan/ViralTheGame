package game;

import org.newdawn.slick.Image;

/**
 * Cell is the repository class that defines the values
 * of a player's cell that is planted during gameplay.
 * @author Hao Guan
 */
public class Cell {
	
	int cellColor; //one of the four player colors in Player.java
	Player player;
	Image cellImage = null;
	Tile currentTile;
	Position currentPosition;
	float[] cellCoords;
	
	
	/**
	 * The two parameter constructor that takes Player and Tile objects.
	 * @param player is the associated player that owns the cell.
	 * @param curTile is the tile location of the cell.
	 */
	public Cell(Player player, Tile curTile) {
		cellColor = player.getColor();
		this.player = player;
		currentTile = curTile;
		currentPosition = curTile.getCellPosition(this); //sets the position already automatically.
	}
	
	/**
	 * setPlayer() sets the Player field with the correct
	 * owner for the cell.
	 * @param player is the owner
	 */
	public void setPlayer(Player player){
		this.player = player;
	}
	
	/**
	 * setImage() sets the cellImage field with the correct
	 * picture for the cell.
	 * @param image is the new image.
	 */
	public void setImage(Image image) {
		cellImage = image;
	}
	
	/**
	 * setCurrentTile() sets the currentTile field
	 * with the correct tile for the cell.
	 * @param tile is new tile object.
	 */
	public void setCurrentTile(Tile tile) {
		currentTile = tile;
	}
		
	/**
	 * setCurrentPosition() sets the correct position
	 * on the tile for rendering purposes.
	 * @param pos is the new position.
	 */
	public void setCurrentPosition(Position pos) {
		currentPosition = pos;
	}
	
	/**
	 * setCellCoords() sets the correct (x, y) coordinates
	 * for rendering the cell.
	 * @param coords are the set of coordinates.
	 */
	public void setCellCoords(float[] coords) {
		cellCoords = coords;
	}
	
	//Getter methods.
	/**
	 * getImage() returns the current cell image.
	 * @return the current cell image.
	 */
	public Image getImage() {
		return cellImage;
	}
	
	/**
	 * getPlayer() returns the player who owns the cell.
	 * @return the player object.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * getCurrentTile() returns the current tile the cell is placed on.
	 * @return the Tile object associated with cell.
	 */
	public Tile getCurrentTile() {
		return currentTile;
	}
	
	/**
	 * getCurrentPosition() returns the position on the tile the 
	 * cell is located.
	 * @return the Position object the cell is associated with.
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * getCellCoords() returns the array of coordinates for
	 * where the cell is located in (x,y).
	 * @return the coordinates of the cell.
	 */
	public float[] getCellCoords() {
		return cellCoords;
	}
	
	
}
