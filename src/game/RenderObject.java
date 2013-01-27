package game;

import java.util.Arrays;

import org.newdawn.slick.Image;

/**
 * RenderObject is used to render game play objects that manifest through certain cards being played.
 * @author Howard Guan
 */
public class RenderObject {
	
	//Integer Representation of types.
	public final static int FIRE = 0;
	public final static int WATER = 1;
	public final static int WIND = 2;
	public final static int EARTH = 3;
	public final static int MARK_OF_THE_VOID = 4;
	public final static int SEEKER = 5;
	public final static int BEACONOFLIGHT = 6;
	public final static int BEACONOFRECLAMATION = 7;
	public final static int BEACONOFHASTE = 8;
	public final static int BEACONOFVOID = 9;
	
	int type;
	Image image;
	Tile tile;
	float[] coords;
	
	public RenderObject(int type, Tile tile, Image image, float[] coords) {
		this.type = type;
		this.tile = tile;
		this.image = image;
		this.coords = coords;
	}


	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public float[] getCoords() {
		return coords;
	}

	public void setCoords(float[] coords) {
		this.coords = coords;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tile.getRingNum();
		result = prime * result + tile.getTileID();
		result = prime * result + Arrays.hashCode(coords);
		result = prime * result + type;
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
		RenderObject other = (RenderObject) obj;
		if (!Arrays.equals(coords, other.coords))
			return false;
		if (type != other.type)
			return false;
		if (tile.getRingNum() != other.tile.getRingNum())
			return false;
		if (tile.getTileID() != other.tile.getTileID())
			return false;
		if (image.getResourceReference() != other.image.getResourceReference())
			return false;
		return true;
	}
	
}
