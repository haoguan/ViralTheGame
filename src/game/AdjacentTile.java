package game;

public class AdjacentTile {
	Tile tile;
	boolean available;

	public AdjacentTile(Tile tile, boolean avail){
		this.tile = tile;
		available = avail;
	}

	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((tile == null) ? 0 : tile.getRingNum());
		result = prime * result + ((tile == null) ? 0 : tile.getTileID());
		return result;
	}

	@Override
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

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (available ? 1231 : 1237);
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		AdjacentTile other = (AdjacentTile) obj;
//		if (available != other.available)
//			return false;
//		if (tile.getRingNum() != other.getTile().getRingNum())
//			return false;
//		if (tile.getTileID() != other.getTile().getTileID()) 
//			return false;
//		return true;
//	}
}
