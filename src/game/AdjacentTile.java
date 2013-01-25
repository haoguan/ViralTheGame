package game;

/**
 * AdjacentTile is the repository class that helps
 * the possible movement algorithm figure out
 * whether a tile can be traversed.
 * @author Hao Guan
 */
public class AdjacentTile {
	
	Tile tile;
	boolean available;

	/**
	 * The two parameter constructor that needs a Tile object
	 * and a boolean for availability.
	 * @param tile is the adjacent tile.
	 * @param avail specifies whether this adjacent tile is
	 * available with respect to current tile.
	 */
	public AdjacentTile(Tile tile, boolean avail){
		this.tile = tile;
		available = avail;
	}

	/**
	 * getTile() returns the associated Tile object. 
	 * @return the Tile object for this adjacent tile.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * setTile() applies a new tile to the tile field.
	 * @param tile is the input tile.
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	/**
	 * isAvailable() returns whether this tile is available.
	 * @return the field available.
	 */
	public boolean isAvailable() {
		return available;
	}
	
	/**
	 * setAvailable() is a setter function that sets the available
	 * field to the input parameter.
	 * @param available is the new boolean.
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * hashCode() generates a new hash code.
	 * @Override the Object default hash code.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((tile == null) ? 0 : tile.getRingNum());
		result = prime * result + ((tile == null) ? 0 : tile.getTileID());
		return result;
	}

	/**
	 * equals() overrides the default equals with new specifications for equality.
	 * @Override the default == equals.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdjacentTile other = (AdjacentTile) obj;
		if (available != other.available)
			return false;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (tile.getRingNum() != other.getTile().getRingNum()) {
			return false;
		} else if (tile.getTileID() != other.getTile().getTileID()) {
			return false;
		}
		return true;
	}

}
